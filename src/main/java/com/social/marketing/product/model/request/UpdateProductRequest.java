package com.social.marketing.product.model.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProductRequest(@NotBlank String name, @NotBlank String description) {
}