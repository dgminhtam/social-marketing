package com.social.marketing.product.service;

import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.request.UpdateProductRequest;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable);

    ProductResponse convert(Product product);

    Product getProductById(Long id);

    ProductDetailResponse getProductDetail(Long id);

    ProductDetailResponse convertDetail(Product product);

    ProductDetailResponse updateProduct(Long id, UpdateProductRequest request);

    void uploadProductImage(Long id, MultipartFile image);
}
