package com.social.marketing.pcm.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.entity.ProductStatus;
import com.social.marketing.pcm.model.response.StorefrontProductDetailResponse;
import com.social.marketing.pcm.model.response.StorefrontProductResponse;
import com.social.marketing.pcm.repository.ProductRepository;
import com.social.marketing.pcm.service.StorefrontCategoryService;
import com.social.marketing.pcm.service.StorefrontProductService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StorefrontProductServiceImpl implements StorefrontProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private MediaService mediaService;

    @Resource
    private StorefrontCategoryService storefrontCategoryService;

    @Override
    public Page<StorefrontProductResponse> getBaseProducts(Specification<Product> specification, Pageable pageable) {
        Specification<Product> spec =
                (root, query, builder) -> builder.equal(root.get(Product.Fields.status), ProductStatus.PUBLISHED);
        Page<Product> products = productRepository.findAll(Objects.nonNull(specification) ? spec.and(specification) : spec, pageable);
        List<StorefrontProductResponse> storefrontProductRespons = products.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(storefrontProductRespons, products.getPageable(), products.getTotalElements());
    }

    @Override
    public Product get(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found.");
        }
        return product.get();
    }

    @Override
    public Product getBySku(String sku) {
        Optional<Product> product = productRepository.findBySku(sku);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found.");
        }
        return product.get();
    }

    @Override
    public StorefrontProductResponse convert(Product product) {
        StorefrontProductResponse response = new StorefrontProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setName(product.getName());
        List<Category> categories = product.getCategories();
        if (CollectionUtils.isEmpty(categories)) {
            response.setCategories(categories.stream().map(storefrontCategoryService::convert).toList());
        }
        response.setImage(mediaService.convert(product.getImage()));
        return response;
    }

    @Override
    public StorefrontProductDetailResponse getProductBySku(String sku) {
        Product product = getBySku(sku);
        return convertDetail(product);
    }

    @Override
    public StorefrontProductDetailResponse convertDetail(Product product) {
        StorefrontProductDetailResponse response = new StorefrontProductDetailResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(Objects.nonNull(product.getPrice()) ? product.getPrice() : BigDecimal.ZERO);
        List<Category> categories = product.getCategories();
        if (CollectionUtils.isEmpty(categories)) {
            response.setCategories(categories.stream().map(storefrontCategoryService::convert).toList());
        }
        response.setImage(mediaService.convert(product.getImage()));
        return response;
    }
}
