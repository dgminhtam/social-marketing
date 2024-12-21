package com.social.marketing.product.model.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Long maxOrderQuantity;
    private Long minOrderQuantity;
    private String category;
}
