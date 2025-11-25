package com.social.marketing.pcm.service;

import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.model.response.StorefrontProductDetailResponse;
import com.social.marketing.pcm.model.response.StorefrontProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface StorefrontProductService {

    Page<StorefrontProductResponse> getProducts(Specification<Product> specification, Pageable pageable);

    Product get(Long id);

    Product getBySku(String sku);

    StorefrontProductResponse convert(Product product);

    StorefrontProductDetailResponse getProductBySku(String sku);

    StorefrontProductDetailResponse convertDetail(Product product);
}
