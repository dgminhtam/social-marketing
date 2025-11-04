package com.social.marketing.pcm.model.response;


import com.social.marketing.media.model.response.MediaResponse;
import com.social.marketing.pcm.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal originPrice;
    private BigDecimal price;
    private ProductStatus status;
    private Long maxOrderQuantity;
    private Long minOrderQuantity;
    private MediaResponse image;
    private CategoryResponse category;
    private String externalId;
}
