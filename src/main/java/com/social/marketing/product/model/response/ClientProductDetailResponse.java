package com.social.marketing.product.model.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ClientProductDetailResponse {

    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Long maxOrderQuantity;
    private Long minOrderQuantity;
    private String mainImage;
    private String category;
    private List<ClientProductDetailResponse> variants;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;
}
