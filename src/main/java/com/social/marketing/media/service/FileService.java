package com.social.marketing.media.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file, String path);

    void deleteFile(String fileName, String path);

    String detachMimeType(MultipartFile file);

    String getExtensionFromMimeType(String mimeType);
}
