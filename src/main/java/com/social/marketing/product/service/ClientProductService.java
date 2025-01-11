package com.social.marketing.product.service;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ClientProductDetailResponse;
import com.social.marketing.product.model.response.ClientProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ClientProductService {

    Page<ClientProductResponse> getBaseProducts(Specification<Product> specification, Pageable pageable);

    Product get(Long id);

    Product getBySku(String sku);

    ClientProductResponse convert(Product product);

    ClientProductDetailResponse getProductBySku(String sku);

    ClientProductDetailResponse convertDetail(Product product);
}
