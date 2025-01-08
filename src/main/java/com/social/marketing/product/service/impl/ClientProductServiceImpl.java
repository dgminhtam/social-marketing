package com.social.marketing.product.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ClientProductDetailResponse;
import com.social.marketing.product.model.response.ClientProductResponse;
import com.social.marketing.product.repository.ProductRepository;
import com.social.marketing.product.service.ClientProductService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientProductServiceImpl implements ClientProductService {

    @Resource
    private ProductRepository productRepository;

    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public Page<ClientProductResponse> getBaseProducts(Specification<Product> specification, Pageable pageable) {
        Specification<Product> spec =
                (root, query, builder) -> builder.isNull(root.get(Product.Fields.base));
        Page<Product> products = productRepository.findAll(spec.and(specification), pageable);
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
    public List<Product> getAllBySkus(List<String> skus) {
        Specification<Product> specification =
                (root, query, builder) -> builder.in(root.get(Product.Fields.sku)).value(skus);
        return productRepository.findAll(specification);
    }

    @Override
    public ClientProductResponse convert(Product product) {
        ClientProductResponse response = new ClientProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setName(product.getName());
        response.setMinOrderQuantity(product.getMinOrderQuantity());
        response.setMaxOrderQuantity(product.getMaxOrderQuantity());
        response.setCategory(product.getCategory().getName());
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
        response.setPrice(product.getPrice());
        response.setMinOrderQuantity(product.getMinOrderQuantity());
        response.setMaxOrderQuantity(product.getMaxOrderQuantity());
        response.setCategory(product.getCategory().getName());
        List<Product> variants = product.getVariants();
        if (CollectionUtils.isNotEmpty(variants)) {
            response.setVariants(variants.stream().map(this::convertDetail).toList());
            response.setLowPrice(variants.get(0).getPrice());
            response.setHighPrice(variants.get(variants.size() - 1).getPrice());
        }
        return response;
    }
}
