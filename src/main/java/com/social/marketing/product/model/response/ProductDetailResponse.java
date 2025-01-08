package com.social.marketing.product.model.response;


import com.social.marketing.media.model.response.MediaResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductDetailResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal originPrice;
    private BigDecimal price;
    private Long maxOrderQuantity;
    private Long minOrderQuantity;
    private MediaResponse image;
    private String category;
    private List<ProductDetailResponse> variants;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;
    private String externalId;
}
