package com.social.marketing.cart.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartEntryResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal subTotal;
    private String imageUrl;
}
