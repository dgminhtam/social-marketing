package com.social.marketing.product.model.request;

import com.social.marketing.product.entity.ProductStatus;

public record ChangeStatusRequest(ProductStatus status) {
}
