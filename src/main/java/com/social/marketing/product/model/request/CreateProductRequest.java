package com.social.marketing.product.model.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(@NotBlank String name, String description, Long categoryId) {
}
