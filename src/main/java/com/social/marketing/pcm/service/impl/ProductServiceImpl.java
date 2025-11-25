package com.social.marketing.pcm.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.model.request.ChangeStatusRequest;
import com.social.marketing.pcm.model.request.CreateProductRequest;
import com.social.marketing.pcm.model.request.UpdateProductRequest;
import com.social.marketing.pcm.model.response.ProductDetailResponse;
import com.social.marketing.pcm.model.response.ProductResponse;
import com.social.marketing.pcm.repository.ProductRepository;
import com.social.marketing.pcm.service.CategoryService;
import com.social.marketing.pcm.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final MediaService mediaService;

    private final CategoryService categoryService;

    @Override
    public Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> clientProductResponse = products.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(clientProductResponse, products.getPageable(), products.getTotalElements());
    }

    @Override
    public ProductResponse convert(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setSlug(product.getSlug());
        response.setDescription(product.getDescription());
        response.setOriginPrice(product.getOriginPrice());
        response.setPrice(product.getPrice());
        response.setName(product.getName());
        response.setImage(mediaService.convert(product.getImage()));
        response.setStatus(product.getStatus());
        List<Category> categories = product.getCategories();
        if (CollectionUtils.isNotEmpty(categories)) {
            response.setCategories(categories.stream().map(categoryService::convert).toList());
        }
        List<Media> gallery = product.getGallery();
        if (CollectionUtils.isNotEmpty(gallery)) {
            response.setGallery(gallery.stream().map(mediaService::convert).toList());
        }
        return response;
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product not found.");
        }
        return productOpt.get();
    }

    @Override
    public ProductDetailResponse getProductDetail(Long id) {
        Product product = getProductById(id);
        return convertDetail(product);
    }

    @Override
    public ProductDetailResponse convertDetail(Product product) {
        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setSlug(product.getSlug());
        response.setDescription(product.getDescription());
        response.setOriginPrice(product.getOriginPrice());
        response.setPrice(product.getPrice());
        response.setName(product.getName());
        response.setImage(mediaService.convert(product.getImage()));
        response.setStatus(product.getStatus());
        List<Category> categories = product.getCategories();
        if (CollectionUtils.isNotEmpty(categories)) {
            response.setCategories(categories.stream().map(categoryService::convert).toList());
        }
        List<Media> gallery = product.getGallery();
        if (CollectionUtils.isNotEmpty(gallery)) {
            response.setGallery(gallery.stream().map(mediaService::convert).toList());
        }
        return response;
    }

    @Override
    public ProductDetailResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = getProductById(id);
        product.setName(request.name());
        product.setSku(request.sku());
        product.setSlug(request.slug());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setOriginPrice(request.originPrice());
        product.setDescription(request.description());
        List<Category> categories = categoryService.getCategoryByIds(request.categoryIds());
        product.setCategories(categories);
        product.setStatus(request.status());
        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            product.setImage(image);
        }
        if (CollectionUtils.isNotEmpty(request.gallery())) {
            List<Media> gallery = mediaService.getAllByIds(request.gallery());
            product.setGallery(gallery);
        }
        productRepository.save(product);
        return convertDetail(product);
    }

    @Override
    @Transactional
    public void uploadProductImage(Long id, MultipartFile image) {
        Product product = getProductById(id);
        if (Objects.nonNull(product.getImage())) {
            Media deleteMedia = product.getImage();
            product.setImage(null);
            productRepository.save(product);
            mediaService.delete(deleteMedia);
        }
        Media media = mediaService.create(image);
        product.setImage(media);
        productRepository.save(product);
    }

    @Override
    public void changeStatus(Long id, ChangeStatusRequest request) {
        Product product = getProductById(id);
        product.setStatus(request.status());
        productRepository.save(product);
    }

    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public List<Product> getAllBySkus(List<String> skus) {
        Specification<Product> specification =
                (root, query, builder) -> builder.in(root.get(Product.Fields.sku)).value(skus);
        return productRepository.findAll(specification);
    }

    @Override
    public void convertUpdate(UpdateProductRequest source, Product target) {
        target.setName(source.name());
        target.setDescription(source.description());
        target.setPrice(source.price());
    }

    @Override
    public ProductDetailResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setSku(request.sku());
        product.setSlug(request.slug());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setOriginPrice(request.originPrice());
        product.setDescription(request.description());
        List<Category> categories = categoryService.getCategoryByIds(request.categoryIds());
        product.setCategories(categories);
        product.setStatus(request.status());
        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            product.setImage(image);
        }
        if (CollectionUtils.isNotEmpty(request.gallery())) {
            List<Media> gallery = mediaService.getAllByIds(request.gallery());
            product.setGallery(gallery);
        }
        productRepository.save(product);
        return convertDetail(product);
    }
}
