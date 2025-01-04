package com.social.marketing.product.service;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ProductService {

    Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable);

    ProductResponse convert(Product product);
}
