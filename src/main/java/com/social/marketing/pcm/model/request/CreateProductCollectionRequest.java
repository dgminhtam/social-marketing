package com.social.marketing.pcm.model.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProductCollectionRequest(
                @NotBlank String name,
                String slug,
                String description,
                String metaTitle,
                String metaDescription,
                String metaKeywords,
                Boolean isFeatured,
                Long imageId,
                Boolean status) {
}
