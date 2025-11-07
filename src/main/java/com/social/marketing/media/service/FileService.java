package com.social.marketing.media.service;

import com.social.marketing.media.model.request.UploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * Hàm chính: Nhận file, detect MIME, validate,
     * upload file gốc, tạo 3 variant WebP, upload 3 variant,
     * và trả về tất cả thông tin.
     */
    UploadResult uploadAndCreateVariants(MultipartFile file);

    /**
     * Xóa file dựa trên URL (logic này cần
     * tùy chỉnh dựa trên nơi bạn lưu trữ file - local hay R2/S3).
     */
    void deleteFileByUrl(String fileUrl);

    // (Hàm này đã bị thay thế bằng logic trong uploadAndCreateVariants)
    String uploadFile(byte[] file, String path);

    void deleteFile(String fileName, String path);

    String detachMimeType(byte[] file);

    // (Hàm này nên là private)
    String getExtensionFromMimeType(String mimeType);

    // Các hàm (detachMimeType, getExtensionFromMimeType)
    // bây giờ có thể là 'private' bên trong FileServiceImpl.
}