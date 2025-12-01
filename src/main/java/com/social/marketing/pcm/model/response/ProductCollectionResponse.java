package com.social.marketing.pcm.model.response;

import com.social.marketing.media.model.response.MediaResponse;
import lombok.Data;

@Data
public class ProductCollectionResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Boolean isFeatured;
    private MediaResponse image;
    private Boolean active;
    private String createdDate;
    private String lastModifiedDate;
}
