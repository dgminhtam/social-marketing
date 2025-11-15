package com.social.marketing.media.model.request;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class UploadResult {
    private String urlOriginal;
    private String mimeType;
    private long fileSizeInByte;
    private String name;
    private Map<String, String> variants;
}