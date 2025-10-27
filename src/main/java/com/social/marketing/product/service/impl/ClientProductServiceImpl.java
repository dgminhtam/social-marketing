package com.social.marketing.product.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.entity.ProductStatus;
import com.social.marketing.product.model.response.ClientProductDetailResponse;
import com.social.marketing.product.model.response.ClientProductResponse;
import com.social.marketing.product.repository.ProductRepository;
import com.social.marketing.product.service.ClientCategoryService;
import com.social.marketing.product.service.ClientProductService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientProductServiceImpl implements ClientProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private MediaService mediaService;

    @Resource
    private ClientCategoryService clientCategoryService;

    @Override
    public Page<ClientProductResponse> getBaseProducts(Specification<Product> specification, Pageable pageable) {
        Specification<Product> spec =
                (root, query, builder) -> builder.equal(root.get(Product.Fields.status), ProductStatus.APPROVED);
        Page<Product> products = productRepository.findAll(Objects.nonNull(specification) ? spec.and(specification) : spec, pageable);
        List<ClientProductResponse> clientProductResponses = products.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(clientProductResponses, products.getPageable(), products.getTotalElements());
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
    public ClientProductResponse convert(Product product) {
        ClientProductResponse response = new ClientProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setName(product.getName());
        Category category = product.getCategory();
        if (Objects.nonNull(category)) {
            response.setCategory(clientCategoryService.convert(category));
        }
        response.setImage(mediaService.convert(product.getImage()));
        return response;
    }

    @Override
    public ClientProductDetailResponse getProductBySku(String sku) {
        Product product = getBySku(sku);
        return convertDetail(product);
    }

    @Override
    public ClientProductDetailResponse convertDetail(Product product) {
        ClientProductDetailResponse response = new ClientProductDetailResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(Objects.nonNull(product.getPrice()) ? product.getPrice() : BigDecimal.ZERO);
        Category category = product.getCategory();
        if (Objects.nonNull(category)) {
            response.setCategory(clientCategoryService.convert(category));
        }
        response.setImage(mediaService.convert(product.getImage()));
        return response;
    }
}
