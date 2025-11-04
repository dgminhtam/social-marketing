package com.social.marketing.pcm.model.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(@NotBlank String name, @NotBlank String description) {
}
