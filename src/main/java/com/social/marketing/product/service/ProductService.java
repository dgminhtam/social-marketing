package com.social.marketing.product.service;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {

    void saveAll(List<Product> products);

    Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable);

    Product get(Long id);

    List<Product> getAllBySkus(List<String> skus);

    Product getBySku(String sku);

    ProductResponse convert(Product product);

    ProductDetailResponse getProductBySku(String sku);

    ProductDetailResponse convertDetail(Product product);
}
