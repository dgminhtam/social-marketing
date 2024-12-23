package com.social.marketing.product.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.response.ProductResponse;
import com.social.marketing.product.repository.ProductRepository;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> productResponses = products.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(productResponses, products.getPageable(), products.getTotalElements());
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
    public ProductResponse convert(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setSku(product.getSku());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setName(product.getName());
        productResponse.setMinOrderQuantity(product.getMinOrderQuantity());
        productResponse.setMaxOrderQuantity(product.getMaxOrderQuantity());
        productResponse.setCategory(product.getCategory().getName());
        return productResponse;
    }
}
