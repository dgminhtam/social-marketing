package com.social.marketing.media.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.model.request.UploadResult;
import com.social.marketing.media.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class R2FileServiceImpl implements FileService {

    private final Tika tika;
    private final MediaProperties properties;
    private final S3Client s3Client;

    @Value("${application.cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${application.cloudflare.r2.public-url}")
    private String publicBucketUrl;

    // Constants config
    private static final int LARGE_SIZE = 1000;
    private static final int MEDIUM_SIZE = 300;
    private static final int THUMBNAIL_SIZE = 150;
    private static final String WEBP_FORMAT = "webp";
    private static final double WEBP_QUALITY = 0.85;
    private static final String WEBP_MIME_TYPE = "image/webp";

    @Override
    public UploadResult uploadAndCreateVariants(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File is empty!");
        }

        byte[] fileBytes;
        String originalMimeType;
        String originalFilenameBase = getFilenameBase(file.getOriginalFilename());

        try {
            fileBytes = file.getBytes();
            originalMimeType = tika.detect(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file content", e);
        }

        validateMimeTypeAndSize(file.getSize(), originalMimeType);

        // Tạo UUID chung cho cả nhóm ảnh (gốc + biến thể) để dễ quản lý
        String uniqueId = UUID.randomUUID().toString();
        String originalExtension = getExtensionFromMimeType(originalMimeType);

        // Danh sách theo dõi các Key đã upload để Rollback nếu lỗi
        List<String> uploadedKeys = new ArrayList<>();
        Map<String, String> variants = new HashMap<>();
        String originalUrl = null;

        try {
            // 1. Upload Original Image
            // Format: originals/{uuid}-{filename}.{ext}
            String originalKey = String.format("originals/%s-%s.%s", uniqueId, originalFilenameBase, originalExtension);
            originalUrl = uploadToR2(fileBytes, originalKey, originalMimeType);
            uploadedKeys.add(originalKey);

            // 2. Resize & Upload Variants
            // Large
            String largeKey = String.format("variants/large-%s-%s.%s", uniqueId, originalFilenameBase, WEBP_FORMAT);
            String largeUrl = resizeAndUpload(fileBytes, LARGE_SIZE, largeKey);
            variants.put("large", largeUrl);
            uploadedKeys.add(largeKey);

            // Medium
            String mediumKey = String.format("variants/medium-%s-%s.%s", uniqueId, originalFilenameBase, WEBP_FORMAT);
            String mediumUrl = resizeAndUpload(fileBytes, MEDIUM_SIZE, mediumKey);
            variants.put("medium", mediumUrl);
            uploadedKeys.add(mediumKey);

            // Thumbnail
            String thumbKey = String.format("variants/thumb-%s-%s.%s", uniqueId, originalFilenameBase, WEBP_FORMAT);
            String thumbUrl = resizeAndUpload(fileBytes, THUMBNAIL_SIZE, thumbKey);
            variants.put("thumbnail", thumbUrl);
            uploadedKeys.add(thumbKey);

        } catch (Exception e) {
            log.error("Error during upload process. Rolling back uploaded files...", e);
            rollbackUploadedFiles(uploadedKeys); // Xóa các file đã lỡ upload
            throw new RuntimeException("Upload failed and rolled back", e);
        }

        return UploadResult.builder()
                .urlOriginal(originalUrl)
                .mimeType(originalMimeType)
                .fileSizeInByte(file.getSize())
                .name(originalFilenameBase)
                .variants(variants)
                .build();
    }

    /**
     * Helper method: Resize và Upload gộp lại để code gọn hơn
     */
    private String resizeAndUpload(byte[] originalBytes, int size, String key) throws IOException {
        byte[] resizedBytes = resizeImageToWebp(originalBytes, size, size);
        return uploadToR2(resizedBytes, key, WEBP_MIME_TYPE);
    }

    private String uploadToR2(byte[] fileBytes, String objectKey, String mimeType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(mimeType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
        return publicBucketUrl + "/" + objectKey;
    }

    /**
     * Xóa file trên R2 dựa vào URL đầy đủ
     */
    @Override
    public void deleteFileByUrl(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(publicBucketUrl)) {
            log.warn("Invalid file URL for deletion: {}", fileUrl);
            return;
        }

        // Trích xuất Object Key từ URL:
        // URL: https://pub-xxx/variants/abc.webp -> Key: variants/abc.webp
        String objectKey = fileUrl.replace(publicBucketUrl + "/", "");
        deleteFileByKey(objectKey);
    }

    private void deleteFileByKey(String objectKey) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3Client.deleteObject(deleteRequest);
            log.info("Deleted file from R2: {}", objectKey);
        } catch (Exception e) {
            log.error("Failed to delete file from R2: {}", objectKey, e);
            // Không throw exception để tránh làm gián đoạn luồng chính (ví dụ luồng xóa bài viết)
        }
    }

    /**
     * Cơ chế Compensation (Bù trừ): Xóa các file đã upload nếu quy trình lỗi
     */
    private void rollbackUploadedFiles(List<String> keys) {
        for (String key : keys) {
            deleteFileByKey(key);
        }
    }

    private byte[] resizeImageToWebp(byte[] originalImage, int width, int height) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(originalImage);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Thumbnails.of(bais)
                    .size(width, height)
                    .keepAspectRatio(true)
                    .outputFormat(WEBP_FORMAT)
                    .outputQuality(WEBP_QUALITY)
                    .toOutputStream(baos);

            return baos.toByteArray();
        }
    }

    // --- Utility Methods ---

    private String getFilenameBase(String filename) {
        if (filename == null) return "image";
        int dotIndex = filename.lastIndexOf('.');
        // Normalize filename: remove special chars to avoid URL issues
        String baseName = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
        return baseName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    @Override
    public String getExtensionFromMimeType(String mimeType) {
        // Mở rộng hỗ trợ nhiều loại hơn nếu cần
        return switch (mimeType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            case "image/bmp" -> "bmp";
            default -> "bin";
        };
    }

    private void validateMimeTypeAndSize(long size, String mimeType) {
        if (size > properties.getMaxSize()) {
            throw new IllegalArgumentException("File exceeds maximum size of " + properties.getMaxSize() + " bytes.");
        }
        if (!properties.getAcceptMimeTypes().contains(mimeType)) {
            throw new IllegalArgumentException("File type not allowed: " + mimeType);
        }
    }

    // --- Deprecated Methods ---

    @Override
    public String uploadFile(byte[] file, String path) {
        throw new UnsupportedOperationException("Deprecated. Use uploadAndCreateVariants instead.");
    }

    @Override
    public void deleteFile(String fileName, String path) {
        // Forward to new delete logic if needed, or leave empty as deprecated
    }

    @Override
    public String detachMimeType(byte[] file) {
        return tika.detect(file);
    }
}