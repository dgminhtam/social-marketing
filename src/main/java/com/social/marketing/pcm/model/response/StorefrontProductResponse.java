package com.social.marketing.pcm.model.response;


import com.social.marketing.media.model.response.MediaResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class StorefrontProductResponse {
    private Long id;
    private String sku;
    private String slug;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImage;
    private List<StorefrontCategoryResponse> categories;
    private MediaResponse image;
}
