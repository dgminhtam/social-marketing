package com.social.marketing.pcm.model.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UpdateProductRequest(@NotBlank String name, String description, BigDecimal price, Long categoryId) {
}