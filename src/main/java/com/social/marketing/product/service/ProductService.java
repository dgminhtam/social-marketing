package com.social.marketing.product.service;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {

    void saveAll(List<Product> products);

    Page<ProductResponse> search(Specification<Product> specification, Pageable pageable);

    Product get(Long id);
}
