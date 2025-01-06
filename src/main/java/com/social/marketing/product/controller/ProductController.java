package com.social.marketing.product.controller;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.request.UpdateProductRequest;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import com.social.marketing.product.service.ProductService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping
    public Page<ProductResponse> getProducts(@Search Specification<Product> specification, Pageable pageable) {
        return productService.getProducts(specification, pageable);
    }

    @GetMapping("/{id}")
    public ProductDetailResponse getProducts(@PathVariable Long id) {
        return productService.getProductDetail(id);
    }

    @PutMapping("/{id}")
    public ProductDetailResponse updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @PostMapping("/{id}/upload-image")
    public void uploadProductImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        productService.uploadProductImage(id, image);
    }

}
