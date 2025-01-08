package com.social.marketing.media.model.response;

import lombok.Data;

@Data
public class MediaResponse {

    private String fileName;

    private String altText;

    private String description;

    private String realFileName;

    private String mimeType;

    private Long fileSizeInByte;

    private String url;
}
