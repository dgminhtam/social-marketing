package com.social.marketing.product.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.entity.ProductStatus;
import com.social.marketing.product.model.request.ChangeStatusRequest;
import com.social.marketing.product.model.request.CreateProductRequest;
import com.social.marketing.product.model.request.UpdateProductRequest;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import com.social.marketing.product.repository.ProductRepository;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private MediaService mediaService;

    @Resource
    private CategoryService categoryService;

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
        response.setDescription(product.getDescription());
        response.setOriginPrice(product.getOriginPrice());
        response.setPrice(product.getPrice());
        response.setName(product.getName());
        response.setImage(mediaService.convert(product.getImage()));
        response.setStatus(product.getStatus());
        Category category = product.getCategory();
        if (Objects.nonNull(category)) {
            response.setCategory(categoryService.convert(category));
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
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setOriginPrice(product.getOriginPrice());
        response.setPrice(product.getPrice());
        response.setImage(mediaService.convert(product.getImage()));
        Category category = product.getCategory();
        if (Objects.nonNull(category)) {
            response.setCategory(categoryService.convert(category));
        }
        response.setStatus(product.getStatus());
        return response;
    }

    @Override
    public ProductDetailResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = getProductById(id);
        convertUpdate(request, product);
        productRepository.save(product);
        return convertDetail(product);
    }

    @Override
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
    public List<ProductResponse> getProductsByCategory(Category category) {
        Specification<Product> specification =
                (root, query, builder) -> builder.equal(root.get(Product.Fields.category), category);
        return productRepository.findAll(specification).stream().map(this::convert).toList();
    }


    @Override
    public ProductDetailResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setSku(UUID.randomUUID().toString());
        product.setDescription(request.description());
        Category category = categoryService.getCategoryById(request.categoryId());
        product.setCategory(category);
        product.setStatus(ProductStatus.DRAFT);
        productRepository.save(product);
        return convertDetail(product);
    }
}
