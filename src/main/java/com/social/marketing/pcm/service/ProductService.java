package com.social.marketing.pcm.service;

import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.model.request.ChangeStatusRequest;
import com.social.marketing.pcm.model.request.CreateProductRequest;
import com.social.marketing.pcm.model.request.UpdateProductRequest;
import com.social.marketing.pcm.model.response.ProductDetailResponse;
import com.social.marketing.pcm.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable);

    ProductResponse convert(Product product);

    Product getProductById(Long id);

    ProductDetailResponse getProductDetail(Long id);

    ProductDetailResponse convertDetail(Product product);

    ProductDetailResponse updateProduct(Long id, UpdateProductRequest request);

    void uploadProductImage(Long id, MultipartFile image);

    void changeStatus(Long id, ChangeStatusRequest request);

    void saveAll(List<Product> products);

    List<Product> getAllBySkus(List<String> skus);

    void convertUpdate(UpdateProductRequest source, Product target);

    ProductDetailResponse createProduct(CreateProductRequest request);

    void importProducts(MultipartFile file);

    void addAlternativeProduct(Long id, Long alternativeId);

    void removeAlternativeProduct(Long id, Long alternativeId);
}
