package com.social.marketing.media.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.model.request.UploadResult;
import com.social.marketing.media.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final Tika tika;
    private final MediaProperties properties;

    private static final int LARGE_SIZE = 1000;
    private static final int MEDIUM_SIZE = 300;
    private static final int THUMBNAIL_SIZE = 150;
    private static final String WEBP_FORMAT = "webp";
    private static final double WEBP_QUALITY = 0.85;

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
        String originalFileName = originalFilenameBase + "-" + UUID.randomUUID() + "." + originalExtension;
        String originalUrl = saveFileToLocal(fileBytes, originalFileName);

        Map<String, String> variants = new HashMap<>();

        try {
            byte[] largeBytes = resizeImageToWebp(fileBytes, LARGE_SIZE, LARGE_SIZE);
            String largeFileName = "large-" + originalFilenameBase + ".webp";
            String largeUrl = saveFileToLocal(largeBytes, largeFileName);
            variants.put("large", largeUrl);

            byte[] mediumBytes = resizeImageToWebp(fileBytes, MEDIUM_SIZE, MEDIUM_SIZE);
            String mediumFileName = "medium-" + originalFilenameBase + ".webp";
            String mediumUrl = saveFileToLocal(mediumBytes, mediumFileName);
            variants.put("medium", mediumUrl);

            byte[] thumbBytes = resizeImageToWebp(fileBytes, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            String thumbFileName = "thumb-" + originalFilenameBase + ".webp";
            String thumbUrl = saveFileToLocal(thumbBytes, thumbFileName);
            variants.put("thumbnail", thumbUrl);

        } catch (IOException e) {
            throw new RuntimeException("Could not resize and upload variants to WebP", e);
        }

        return UploadResult.builder()
                .urlOriginal(originalUrl)
                .mimeType(originalMimeType)
                .fileSizeInByte(file.getSize())
                .altText(originalFilenameBase)
                .variants(variants)
                .build();
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {

    }

    private String saveFileToLocal(byte[] fileBytes, String newFileName) {
        String relativePath = properties.getUploadDir();
        Path filePath = Paths.get(relativePath, newFileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file.", e);
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(relativePath)
                .path("/")
                .path(newFileName)
                .toUriString();
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