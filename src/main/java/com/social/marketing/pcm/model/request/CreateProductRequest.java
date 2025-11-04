package com.social.marketing.pcm.model.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(@NotBlank String name, String description, Long categoryId) {
}
