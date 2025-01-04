package com.social.marketing.product.controller;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ClientProductDetailResponse;
import com.social.marketing.product.model.response.ClientProductResponse;
import com.social.marketing.product.service.ClientProductService;
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
@RequestMapping("/client/products")
public class ClientProductController {

    @Resource
    private ClientProductService clientProductService;

    @GetMapping
    public Page<ClientProductResponse> getProducts(@Search Specification<Product> specification, Pageable pageable) {
        return clientProductService.getBaseProducts(specification, pageable);
    }

    @GetMapping("/{sku}")
    public ClientProductDetailResponse getProduct(@PathVariable String sku) {
        return clientProductService.getProductBySku(sku);
    }
}
