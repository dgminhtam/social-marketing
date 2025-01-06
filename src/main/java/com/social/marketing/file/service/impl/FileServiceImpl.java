package com.social.marketing.file.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.file.service.FileService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File empty!");
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;

        Path filePath = Paths.get(uploadDir, newFileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/images/")
                .path(newFileName)
                .toUriString();
    }
}
