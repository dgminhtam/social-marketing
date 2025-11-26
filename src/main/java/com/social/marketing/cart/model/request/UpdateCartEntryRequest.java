package com.social.marketing.cart.model.request;

import jakarta.validation.constraints.Positive;

public record UpdateCartEntryRequest(
        @Positive(message = "Số lượng phải lớn hơn 0") Long quantity) {
}
