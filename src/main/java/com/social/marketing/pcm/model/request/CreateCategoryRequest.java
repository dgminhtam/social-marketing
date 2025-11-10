package com.social.marketing.pcm.model.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(@NotBlank String name, @NotBlank String slug, @NotBlank String description, Long imageId, Long parentId, boolean active) {
}
