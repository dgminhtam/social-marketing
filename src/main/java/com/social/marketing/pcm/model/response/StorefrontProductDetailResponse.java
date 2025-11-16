package com.social.marketing.pcm.model.response;


import com.social.marketing.media.model.response.MediaResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class StorefrontProductDetailResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private MediaResponse image;
    private List<StorefrontCategoryResponse> categories;
}
