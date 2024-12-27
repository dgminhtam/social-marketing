package com.social.marketing.product.controller;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import com.social.marketing.product.service.ProductService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping
    public Page<ProductResponse> getProducts(@Search Specification<Product> specification, Pageable pageable) {
        return productService.getProducts(specification, pageable);
    }

    @GetMapping("/{sku}")
    public ProductDetailResponse getProduct(@PathVariable String sku) {
        return productService.getProductBySku(sku);
    }
}
