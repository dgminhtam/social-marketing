package com.social.marketing.cart.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.lang.NonNull;

import java.util.List;

public record PlaceOrderRequest(@NonNull String link, @NonNull @Email String email,
                                @Valid List<OrderEntryRequest> entries,
                                String description) {
}
