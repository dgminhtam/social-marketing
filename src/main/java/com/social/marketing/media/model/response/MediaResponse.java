package com.social.marketing.media.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaResponse {
    private long id;
    private String name;
    private String altText;
    private String urlOriginal;
    private String urlLarge;
    private String urlMedium;
    private String urlThumbnail;
    private Long size;
}
