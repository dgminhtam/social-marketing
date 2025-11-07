package com.social.marketing.media.model.request;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder // Dùng Builder Pattern để dễ tạo object
public class UploadResult {
    private String urlOriginal;
    private String mimeType;
    private long fileSizeInByte;
    private String altText; // Tên file gốc (không đuôi)
    private Map<String, String> variants; // Map của các URL (large, medium, thumb)
}