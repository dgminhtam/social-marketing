package com.social.marketing.pcm.service;

import com.social.marketing.pcm.entity.ProductCollection;
import com.social.marketing.pcm.model.request.CreateProductCollectionRequest;
import com.social.marketing.pcm.model.request.UpdateProductCollectionRequest;
import com.social.marketing.pcm.model.response.ProductCollectionDetailResponse;
import com.social.marketing.pcm.model.response.ProductCollectionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductCollectionService {

    Page<ProductCollectionResponse> getCollections(Specification<ProductCollection> specification, Pageable pageable);

    ProductCollectionDetailResponse getCollection(Long id);

    ProductCollectionDetailResponse getCollectionBySlug(String slug);

    ProductCollectionDetailResponse createCollection(CreateProductCollectionRequest request);

    ProductCollectionDetailResponse updateCollection(Long id, UpdateProductCollectionRequest request);

    void deleteCollection(Long id);

    void addProductsToCollection(Long collectionId, List<Long> productIds);

    void removeProductsFromCollection(Long collectionId, List<Long> productIds);

    ProductCollectionResponse convert(ProductCollection collection);
}
