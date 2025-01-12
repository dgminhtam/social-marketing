package com.social.marketing.order.model.request;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.NonNull;

public record OrderEntryRequest(@NonNull String sku, @NonNull @PositiveOrZero Long quantity,
                                String description) {
}
