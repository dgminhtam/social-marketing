package com.social.marketing.pcm.controller;

import com.social.marketing.pcm.entity.ProductCollection;
import com.social.marketing.pcm.model.request.CreateProductCollectionRequest;
import com.social.marketing.pcm.model.request.UpdateProductCollectionRequest;
import com.social.marketing.pcm.model.response.ProductCollectionDetailResponse;
import com.social.marketing.pcm.model.response.ProductCollectionResponse;
import com.social.marketing.pcm.service.ProductCollectionService;
import com.social.marketing.search.anotation.Search;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-collections")
@RequiredArgsConstructor
public class ProductCollectionController {

    private final ProductCollectionService productCollectionService;

    @GetMapping
    public Page<ProductCollectionResponse> getCollections(@Search Specification<ProductCollection> specification,
            Pageable pageable) {
        return productCollectionService.getCollections(specification, pageable);
    }

    @GetMapping("/{id}")
    public ProductCollectionDetailResponse getCollection(@PathVariable Long id) {
        return productCollectionService.getCollection(id);
    }

    @PostMapping
    public ProductCollectionDetailResponse createCollection(
            @RequestBody @Valid CreateProductCollectionRequest request) {
        return productCollectionService.createCollection(request);
    }

    @PutMapping("/{id}")
    public ProductCollectionDetailResponse updateCollection(@PathVariable Long id,
            @RequestBody @Valid UpdateProductCollectionRequest request) {
        return productCollectionService.updateCollection(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        productCollectionService.deleteCollection(id);
    }

    @PostMapping("/{id}/products")
    public void addProductsToCollection(@PathVariable Long id, @RequestBody List<Long> productIds) {
        productCollectionService.addProductsToCollection(id, productIds);
    }

    @DeleteMapping("/{id}/products")
    public void removeProductsFromCollection(@PathVariable Long id, @RequestBody List<Long> productIds) {
        productCollectionService.removeProductsFromCollection(id, productIds);
    }
}
