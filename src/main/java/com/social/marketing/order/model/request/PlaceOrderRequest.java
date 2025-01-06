package com.social.marketing.order.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.NonNull;

public record PlaceOrderRequest(@NonNull String link, @NonNull @Email String email, @NonNull String sku, @NonNull @PositiveOrZero Long quantity,
                                String description) {
}
