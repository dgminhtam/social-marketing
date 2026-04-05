package com.social.marketing.pcm.controller;

import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.model.response.StorefrontProductDetailResponse;
import com.social.marketing.pcm.model.response.StorefrontProductResponse;
import com.social.marketing.pcm.service.StorefrontProductService;
import com.social.marketing.search.anotation.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storefront/products")
@RequiredArgsConstructor
public class StorefrontProductController {

    private final StorefrontProductService storefrontProductService;

    @GetMapping
    public Page<StorefrontProductResponse> getProducts(@Search Specification<Product> specification, Pageable pageable) {
        return storefrontProductService.getProducts(specification, pageable);
    }

    @GetMapping("/{sku}")
    public StorefrontProductDetailResponse getProduct(@PathVariable String sku) {
        return storefrontProductService.getProductBySku(sku);
    }
}
