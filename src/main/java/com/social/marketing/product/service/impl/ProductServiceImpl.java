package com.social.marketing.product.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhServiceResponse;
import com.social.marketing.integration.marketingxanh.service.MarketingxanhService;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.entity.ProductStatus;
import com.social.marketing.product.model.request.ChangeStatusRequest;
import com.social.marketing.product.model.request.UpdateProductRequest;
import com.social.marketing.product.model.response.ProductDetailResponse;
import com.social.marketing.product.model.response.ProductResponse;
import com.social.marketing.product.repository.ProductRepository;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private MediaService mediaService;

    @Resource
    private MarketingxanhService marketingxanhService;

    @Override
    public Page<ProductResponse> getProducts(Specification<Product> specification, Pageable pageable) {
        Specification<Product> spec =
                (root, query, builder) -> builder.isNull(root.get(Product.Fields.base));
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
        response.setCategory(product.getCategory().getName());
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
    public void syncProducts() {
        List<MarketingxanhServiceResponse> responses = marketingxanhService.getServices();
        if (responses.isEmpty()) {
            throw new RuntimeException("No marketingxanh services found.");
        }

        List<String> externalIds = responses.stream()
                .map(MarketingxanhServiceResponse::getService)
                .toList();

        List<Product> existingProducts = productRepository.findByExternalIdIn(externalIds);

        if (existingProducts.isEmpty()) {
            throw new RuntimeException("No matching products found in the database.");
        }

        Map<String, MarketingxanhServiceResponse> responseMap = responses.stream()
                .collect(Collectors.toMap(MarketingxanhServiceResponse::getService, response -> response));

        existingProducts.forEach(product -> {
            MarketingxanhServiceResponse matchingResponse = responseMap.get(product.getExternalId());
            if (matchingResponse != null) {
                product.setName(matchingResponse.getName());
                product.setMinOrderQuantity(matchingResponse.getMin());
                product.setMaxOrderQuantity(matchingResponse.getMax());
            }
        });
        productRepository.saveAll(existingProducts);
    }


    private void convertUpdate(UpdateProductRequest source, Product target) {
        target.setName(source.name());
        target.setDescription(source.description());
        target.setPrice(source.price());
    }
}
