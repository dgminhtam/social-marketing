package com.social.marketing.pcm.service.impl;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.entity.ProductCollection;
import com.social.marketing.pcm.model.request.CreateProductCollectionRequest;
import com.social.marketing.pcm.model.request.UpdateProductCollectionRequest;
import com.social.marketing.pcm.model.response.ProductCollectionDetailResponse;
import com.social.marketing.pcm.model.response.ProductCollectionResponse;
import com.social.marketing.pcm.repository.ProductCollectionRepository;
import com.social.marketing.pcm.repository.ProductRepository;
import com.social.marketing.pcm.service.ProductCollectionService;
import com.social.marketing.pcm.service.ProductService;
import com.social.marketing.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCollectionServiceImpl implements ProductCollectionService {

    private final ProductCollectionRepository productCollectionRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final MediaService mediaService;

    @Override
    public Page<ProductCollectionResponse> getCollections(Specification<ProductCollection> specification,
            Pageable pageable) {
        Page<ProductCollection> collections = productCollectionRepository.findAll(specification, pageable);
        List<ProductCollectionResponse> responses = collections.getContent().stream()
                .map(this::convert)
                .toList();
        return new PageImpl<>(responses, pageable, collections.getTotalElements());
    }

    @Override
    public ProductCollectionDetailResponse getCollection(Long id) {
        ProductCollection collection = findById(id);
        return convertDetail(collection);
    }

    @Override
    public ProductCollectionDetailResponse getCollectionBySlug(String slug) {
        ProductCollection collection = productCollectionRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Product Collection not found with slug: " + slug));
        return convertDetail(collection);
    }

    @Override
    @Transactional
    public ProductCollectionDetailResponse createCollection(CreateProductCollectionRequest request) {
        ProductCollection collection = new ProductCollection();
        collection.setName(request.name());

        String slug = request.slug();
        if (StringUtils.isBlank(slug)) {
            slug = SlugUtils.toSlug(request.name());
        }
        validateSlug(slug, null);
        collection.setSlug(slug);

        collection.setDescription(request.description());
        collection.setMetaTitle(request.metaTitle());
        collection.setMetaDescription(request.metaDescription());
        collection.setMetaKeywords(request.metaKeywords());
        collection.setIsFeatured(request.isFeatured() != null ? request.isFeatured() : false);
        collection.setStatus(request.status());

        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            collection.setImage(image);
        }

        productCollectionRepository.save(collection);
        return convertDetail(collection);
    }

    @Override
    @Transactional
    public ProductCollectionDetailResponse updateCollection(Long id, UpdateProductCollectionRequest request) {
        ProductCollection collection = findById(id);
        collection.setName(request.name());

        String slug = request.slug();
        if (StringUtils.isBlank(slug)) {
            slug = SlugUtils.toSlug(request.name());
        }
        validateSlug(slug, id);
        collection.setSlug(slug);

        collection.setDescription(request.description());
        collection.setMetaTitle(request.metaTitle());
        collection.setMetaDescription(request.metaDescription());
        collection.setMetaKeywords(request.metaKeywords());
        collection.setIsFeatured(request.isFeatured() != null ? request.isFeatured() : false);
        collection.setStatus(request.status());

        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            collection.setImage(image);
        }

        productCollectionRepository.save(collection);
        return convertDetail(collection);
    }

    @Override
    @Transactional
    public void deleteCollection(Long id) {
        ProductCollection collection = findById(id);
        productCollectionRepository.delete(collection);
    }

    @Override
    @Transactional
    public void addProductsToCollection(Long collectionId, List<Long> productIds) {
        ProductCollection collection = findById(collectionId);
        List<Product> products = productRepository.findAllById(productIds);
        collection.getProducts().addAll(products);
        productCollectionRepository.save(collection);
    }

    @Override
    @Transactional
    public void removeProductsFromCollection(Long collectionId, List<Long> productIds) {
        ProductCollection collection = findById(collectionId);
        List<Product> productsToRemove = productRepository.findAllById(productIds);
        collection.getProducts().removeAll(productsToRemove);
        productCollectionRepository.save(collection);
    }

    @Override
    public ProductCollectionResponse convert(ProductCollection collection) {
        ProductCollectionResponse response = new ProductCollectionResponse();
        response.setId(collection.getId());
        response.setName(collection.getName());
        response.setSlug(collection.getSlug());
        response.setDescription(collection.getDescription());
        response.setMetaTitle(collection.getMetaTitle());
        response.setMetaDescription(collection.getMetaDescription());
        response.setMetaKeywords(collection.getMetaKeywords());
        response.setIsFeatured(collection.getIsFeatured());
        response.setStatus(collection.getStatus());
        if (collection.getImage() != null) {
            response.setImage(mediaService.convert(collection.getImage()));
        }
        response.setCreatedDate(collection.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        response.setLastModifiedDate(collection.getLastModifiedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        return response;
    }

    private ProductCollectionDetailResponse convertDetail(ProductCollection collection) {
        ProductCollectionDetailResponse response = new ProductCollectionDetailResponse();
        response.setId(collection.getId());
        response.setName(collection.getName());
        response.setSlug(collection.getSlug());
        response.setDescription(collection.getDescription());
        response.setMetaTitle(collection.getMetaTitle());
        response.setMetaDescription(collection.getMetaDescription());
        response.setMetaKeywords(collection.getMetaKeywords());
        response.setIsFeatured(collection.getIsFeatured());
        response.setStatus(collection.getStatus());
        if (collection.getImage() != null) {
            response.setImage(mediaService.convert(collection.getImage()));
        }
        response.setProducts(collection.getProducts().stream()
                .map(productService::convert)
                .toList());
        response.setCreatedDate(collection.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        response.setLastModifiedDate(collection.getLastModifiedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        return response;
    }

    private ProductCollection findById(Long id) {
        return productCollectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Collection not found with id: " + id));
    }

    private void validateSlug(String slug, Long id) {
        Optional<ProductCollection> existing = productCollectionRepository.findBySlug(slug);
        if (existing.isPresent()) {
            if (id == null || !existing.get().getId().equals(id)) {
                throw new BadRequestException("Slug already exists: " + slug);
            }
        }
    }
}
