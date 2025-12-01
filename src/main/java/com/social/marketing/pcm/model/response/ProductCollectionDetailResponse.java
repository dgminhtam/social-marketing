package com.social.marketing.pcm.model.response;

import com.social.marketing.media.model.response.MediaResponse;
import lombok.Data;

import java.util.List;

@Data
public class ProductCollectionDetailResponse {
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
    private List<ProductResponse> products;
    private String createdDate;
    private String lastModifiedDate;
}
