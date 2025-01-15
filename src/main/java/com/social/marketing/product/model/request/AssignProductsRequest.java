package com.social.marketing.product.model.request;

import java.util.List;

public record AssignProductsRequest(List<Long> variantIds) {
}
