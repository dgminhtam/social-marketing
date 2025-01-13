package com.social.marketing.product.model.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UpdateProductRequest(@NotBlank String name, String description, BigDecimal price, Long categoryId) {
}