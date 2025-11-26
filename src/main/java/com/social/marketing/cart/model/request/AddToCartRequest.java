package com.social.marketing.cart.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddToCartRequest(
        @NotBlank(message = "SKU không được để trống") String sku,

        @Positive(message = "Số lượng phải lớn hơn 0") Long quantity,

        String description) {
}
