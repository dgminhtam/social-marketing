package com.social.marketing.product.model.response;


import com.social.marketing.media.model.response.MediaResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ClientProductResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Long maxOrderQuantity;
    private Long minOrderQuantity;
    private String mainImage;
    private ClientCategoryResponse category;
    private MediaResponse image;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;
}
