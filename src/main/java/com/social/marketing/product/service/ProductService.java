package com.social.marketing.product.service;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.model.request.AssignProductsRequest;
import com.social.marketing.product.model.request.ChangeStatusRequest;
import com.social.marketing.product.model.request.CreateProductRequest;
import com.social.marketing.product.model.request.UpdateProductRequest;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
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

    List<Product> getAllBySkus(List<String> skus);

    void saveAll(List<Product> products);

    void syncProducts();

    void convertUpdate(UpdateProductRequest source, Product target);

    List<ProductResponse> getProductsByCategory(Category category);

    List<ProductResponse> getProductsUnassignment();

    void assignProducts(Long id, AssignProductsRequest request);

    ProductDetailResponse createProduct(CreateProductRequest request);
}
