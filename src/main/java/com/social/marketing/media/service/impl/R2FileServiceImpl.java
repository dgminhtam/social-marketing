package com.social.marketing.media.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.model.request.UploadResult;
import com.social.marketing.media.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class R2FileServiceImpl implements FileService {

    private final Tika tika;
    private final MediaProperties properties;

    // 3. Lấy thông tin bucket từ application.yml
    @Value("${application.cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${application.cloudflare.r2.public-url}")
    private String publicBucketUrl; // URL công khai của R2 (https://pub-...)

    private final S3Client s3Client;

    private static final int LARGE_SIZE = 1000;
    private static final int MEDIUM_SIZE = 300;
    private static final int THUMBNAIL_SIZE = 150;
    private static final String WEBP_FORMAT = "webp";
    private static final double WEBP_QUALITY = 0.85;
    private static final String WEBP_MIME_TYPE = "image/webp";

    @Override
    @Transactional
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
            throw new RuntimeException("Could not read file", e);
        }

        validateMimeTypeAndSize(file.getSize(), originalMimeType);

        String originalExtension = getExtensionFromMimeType(originalMimeType);
        String originalObjectKey = "originals/" + originalFilenameBase + "-" + UUID.randomUUID() + "." + originalExtension;
        String originalUrl = uploadToR2(fileBytes, originalObjectKey, originalMimeType);

        Map<String, String> variants = new HashMap<>();

        try {
            byte[] largeBytes = resizeImageToWebp(fileBytes, LARGE_SIZE, LARGE_SIZE);
            String largeKey = "variants/large-" + originalFilenameBase + ".webp";
            String largeUrl = uploadToR2(largeBytes, largeKey, WEBP_MIME_TYPE);
            variants.put("large", largeUrl);

            // Medium (WebP)
            byte[] mediumBytes = resizeImageToWebp(fileBytes, MEDIUM_SIZE, MEDIUM_SIZE);
            String mediumKey = "variants/medium-" + originalFilenameBase + ".webp";
            String mediumUrl = uploadToR2(mediumBytes, mediumKey, WEBP_MIME_TYPE);
            variants.put("medium", mediumUrl);

            // Thumbnail (WebP)
            byte[] thumbBytes = resizeImageToWebp(fileBytes, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            String thumbKey = "variants/thumb-" + originalFilenameBase + ".webp";
            String thumbUrl = uploadToR2(thumbBytes, thumbKey, WEBP_MIME_TYPE);
            variants.put("thumbnail", thumbUrl);

        } catch (IOException e) {
            throw new RuntimeException("Could not resize and upload variants to WebP", e);
        }

        return UploadResult.builder()
                .urlOriginal(originalUrl)
                .mimeType(originalMimeType)
                .fileSizeInByte(file.getSize())
                .name(originalFilenameBase)
                .variants(variants)
                .build();
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {

    }

    /**
     * 4. Hàm private mới: uploadToR2 (thay thế saveFileToLocal)
     */
    private String uploadToR2(byte[] fileBytes, String objectKey, String mimeType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey) // Tên file (đường dẫn) trên R2
                .contentType(mimeType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

        // 5. Trả về URL công khai (Public URL)
        // Rất quan trọng: R2 dùng URL public khác với endpoint API
        return publicBucketUrl + "/" + objectKey;
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

    @Override
    public String uploadFile(byte[] file, String path) {
        throw new UnsupportedOperationException("Deprecated. Use uploadAndCreateVariants instead.");
    }

    @Override
    public void deleteFile(String fileName, String path) {
    }

    @Override
    public String detachMimeType(byte[] file) {
        return tika.detect(file);
    }

    @Override
    public String getExtensionFromMimeType(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> "bin";
        };
    }

    private String getFilenameBase(String filename) {
        if (filename == null) {
            return "image";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    private void validateMimeTypeAndSize(long size, String mimeType) {
        if (size > properties.getMaxSize()) {
            throw new IllegalArgumentException("File exceeds maximum size of " + properties.getMaxSize() + " bytes.");
        }
        if (!properties.getAcceptMimeTypes().contains(mimeType)) {
            throw new IllegalArgumentException("File type not allowed: " + mimeType);
        }
    }
}