package com.social.marketing.product.service.impl;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhServiceResponse;
import com.social.marketing.integration.marketingxanh.service.MarketingxanhService;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.entity.ProductStatus;
import com.social.marketing.product.model.request.AssignProductsRequest;
import com.social.marketing.product.model.request.ChangeStatusRequest;
import com.social.marketing.product.model.request.CreateProductRequest;
import com.social.marketing.product.model.request.UpdateProductRequest;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import com.social.marketing.product.repository.ProductRepository;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private MediaService mediaService;

    @Resource
    private MarketingxanhService marketingxanhService;

    @Resource
    private CategoryService categoryService;

    @Override
    public Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable) {
        Specification<Product> spec =
                (root, query, builder) -> builder.isTrue(root.get(Product.Fields.isBase));
        Page<Product> products = productRepository.findAll(spec.and(specification), pageable);
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
        response.setMinOrderQuantity(product.getMinOrderQuantity());
        response.setMaxOrderQuantity(product.getMaxOrderQuantity());
        response.setImage(mediaService.convert(product.getImage()));
        response.setStatus(product.getStatus());
        Category category = product.getCategory();
        if (Objects.nonNull(category)) {
            response.setCategory(categoryService.convert(category));
        }
        response.setExternalId(product.getExternalId());
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
        response.setMinOrderQuantity(product.getMinOrderQuantity());
        response.setMaxOrderQuantity(product.getMaxOrderQuantity());
        response.setImage(mediaService.convert(product.getImage()));
        Category category = product.getCategory();
        if (Objects.nonNull(category)) {
            response.setCategory(categoryService.convert(category));
        }
        response.setStatus(product.getStatus());
        List<Product> variants = product.getVariants();
        if (CollectionUtils.isNotEmpty(variants)) {
            response.setVariants(variants.stream().map(this::convertDetail).toList());
            response.setLowPrice(variants.get(0).getPrice());
            response.setHighPrice(variants.get(variants.size() - 1).getPrice());
        }
        response.setExternalId(product.getExternalId());
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
        List<Product> updateProducts = product.getVariants();
        if (Objects.isNull(product.getBase()) && (ProductStatus.DRAFT.equals(request.status()) || ProductStatus.ARCHIVE.equals(request.status()))) {
            List<Product> variants = product.getVariants();
            if (CollectionUtils.isNotEmpty(variants)) {
                variants.forEach(variant -> variant.setStatus(request.status()));
            }
        }
        product.setStatus(request.status());
        updateProducts.add(product);
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
    @Transactional
    public void syncProducts() {
        List<MarketingxanhServiceResponse> responses = marketingxanhService.getServices();
        if (responses.isEmpty()) {
            throw new NotFoundException("No services found.");
        }

        List<String> externalIds = responses.stream()
                .map(MarketingxanhServiceResponse::getService)
                .toList();

        List<Product> existingProducts = productRepository.findByExternalIdIn(externalIds);

        Map<String, Product> existingProductMap = existingProducts.stream()
                .collect(Collectors.toMap(Product::getExternalId, product -> product));

        List<Product> newProducts = new ArrayList<>();
        responses.forEach(response -> {
            Product product = existingProductMap.get(response.getService());
            if (product != null) {
                updateProductWithResponse(product, response);
            } else {
                newProducts.add(createProductFromResponse(response));
            }
        });

        productRepository.saveAll(existingProducts);

        if (!newProducts.isEmpty()) {
            productRepository.saveAll(newProducts);
        }
    }

    private void updateProductWithResponse(Product product, MarketingxanhServiceResponse response) {
        product.setName(response.getName());
        product.setMinOrderQuantity(response.getMin());
        product.setMaxOrderQuantity(response.getMax());
        product.setOriginPrice(response.getRate());
        product.setDescription(response.getCategory());
    }

    private Product createProductFromResponse(MarketingxanhServiceResponse response) {
        Product product = new Product();
        product.setSku(UUID.randomUUID().toString());
        product.setName(response.getName());
        product.setDescription(response.getDesc());
        product.setMinOrderQuantity(response.getMin());
        product.setMaxOrderQuantity(response.getMax());
        product.setOriginPrice(response.getRate());
        product.setStatus(ProductStatus.DRAFT);
        product.setExternalId(response.getService());
        return product;
    }

    @Override
    public void convertUpdate(UpdateProductRequest source, Product target) {
        target.setName(source.name());
        target.setDescription(source.description());
        target.setPrice(source.price());
        Category category = categoryService.getCategoryById(source.categoryId());
        target.setCategory(category);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Category category) {
        Specification<Product> specification =
                (root, query, builder) -> {
                    Predicate byBase = builder.isNull(root.get(Product.Fields.base));
                    Predicate byStatus = builder.equal(root.get(Product.Fields.category), category);
                    return builder.and(byBase, byStatus);
                };
        return productRepository.findAll(specification).stream().map(this::convert).toList();
    }

    @Override
    public List<ProductResponse> getProductsUnassignment() {
        Specification<Product> specification =
                (root, query, builder) -> {
                    Predicate byBase = builder.isNull(root.get(Product.Fields.base));
                    Predicate byExternalId = builder.isNotNull(root.get(Product.Fields.externalId));
                    return builder.and(byBase, byExternalId);
                };
        return productRepository.findAll(specification).stream().map(this::convert).toList();
    }

    @Override
    public void assignProducts(Long id, AssignProductsRequest request) {
        Product product = getProductById(id);
        List<Long> variantIds = request.variantIds();
        Specification<Product> specification =
                (root, query, builder) -> {
                    Predicate byBase = builder.isNull(root.get(Product.Fields.base));
                    Predicate byStatus = builder.isNotNull(root.get(Product.Fields.externalId));
                    Predicate byIds = builder.in(root.get(AbstractEntity.Fields.id)).value(variantIds);
                    return builder.and(byBase, byStatus, byIds);
                };
        List<Product> products = productRepository.findAll(specification);
        products.forEach(p -> p.setBase(product));
        product.getVariants().addAll(products);
        productRepository.save(product);
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
        product.setIsBase(true);
        productRepository.save(product);
        return convertDetail(product);
    }
}
