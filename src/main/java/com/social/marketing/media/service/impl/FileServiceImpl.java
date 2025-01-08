package com.social.marketing.media.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.service.FileService;
import jakarta.annotation.Resource;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private Tika tika;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        String mimeType = detachMimeType(file);
        String fileExtension = getExtensionFromMimeType(mimeType);
        String newFileName = UUID.randomUUID() + "." + fileExtension;
        Path filePath = Paths.get(path, newFileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file.", e);
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .path(newFileName)
                .toUriString();
    }

    @Override
    public void deleteFile(String fileName, String path) {
        try {
            Path filePath = Paths.get(path, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new NotFoundException("File not found: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        } catch (Exception ignored) {
        }
    }

    @Override
    public String detachMimeType(MultipartFile file) {
        String mimeType;
        try {
            mimeType = tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error detecting file type.", e);
        }
        return mimeType;
    }

    @Override
    public String getExtensionFromMimeType(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        };
    }
}