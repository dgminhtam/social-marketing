package com.social.marketing.pcm.model.request;

import com.social.marketing.pcm.entity.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProductRequest(

    @NotBlank(message = "Tên không được để trống")
    String name,

    @NotBlank(message = "SKU không được để trống")
    String sku,

    @NotBlank(message = "Slug không được để trống")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug chỉ được chứa chữ thường, số và dấu gạch ngang")
    String slug,

    @NotBlank(message = "Mô tả không được để trống")
    String description,

    ProductStatus status,
    List<Long> gallery,
    BigDecimal price,
    BigDecimal originPrice,
    List<Long> categoryIds,
    Long imageId
) {
}