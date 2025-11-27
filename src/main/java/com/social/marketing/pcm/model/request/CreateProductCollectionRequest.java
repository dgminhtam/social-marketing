package com.social.marketing.pcm.model.request;

import com.social.marketing.pcm.entity.ProductCollection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductCollectionRequest(
        @NotBlank String name,
        String slug,
        String description,
        String metaTitle,
        String metaDescription,
        String metaKeywords,
        Boolean isFeatured,
        Long imageId,
        @NotNull ProductCollection.SetStatus status) {
}
