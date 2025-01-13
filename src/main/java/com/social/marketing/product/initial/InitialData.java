package com.social.marketing.product.initial;

import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhServiceResponse;
import com.social.marketing.integration.marketingxanh.service.MarketingxanhService;
import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InitialData {

    @Resource
    private MarketingxanhService marketingxanhService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductService productService;

    @PostConstruct
    @Transactional
    public void init() {
        List<MarketingxanhServiceResponse> responses = marketingxanhService.getServices();
        if (responses.isEmpty()) {
            throw new RuntimeException("No services found.");
        }
        Map<String, Map<String, List<MarketingxanhServiceResponse>>> data = responses.stream()
                .collect(Collectors.groupingBy(
                        response -> response.getCategory().split("\\|")[1].toUpperCase().trim(),
                        Collectors.groupingBy(MarketingxanhServiceResponse::getCategory)));
        Map<String, Category> rootCategoriesExited = categoryService.getAllByNames(data.keySet())
                .stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));

        List<Category> rootCategories = new ArrayList<>();
        data.forEach((rootCategoryName, categoryMap) -> {
            Category rootCategory = rootCategoriesExited.get(rootCategoryName);
            if (Objects.isNull(rootCategory)) {
                rootCategory = new Category();
                rootCategory.setName(rootCategoryName);
                rootCategory.setDescription("View all services of " + rootCategoryName);
                rootCategories.add(rootCategory);
            }
        });
        if (CollectionUtils.isNotEmpty(rootCategories)) {
            categoryService.saveAll(rootCategories);
        }
        List<Product> products = new ArrayList<>();
        rootCategories.forEach(category -> {
            Map<String, List<MarketingxanhServiceResponse>> categoryMap = data.get(category.getName());
            products.addAll(handleBaseProduct(categoryMap, category));
        });
        if (CollectionUtils.isNotEmpty(rootCategories)) {
            productService.saveAll(products);
        }
    }

    private List<Product> handleBaseProduct(Map<String, List<MarketingxanhServiceResponse>> data, Category category) {
        Map<String, Product> exitBaseProductMap = productService.getAllBySkus(data.keySet().stream().toList())
                .stream()
                .collect(Collectors.toMap(Product::getName, Function.identity()));

        List<Product> products = new ArrayList<>();
        data.forEach((name, variants) -> {
            Product product = exitBaseProductMap.get(name);
            if (Objects.isNull(product)) {
                product = new Product();
                product.setSku(UUID.randomUUID().toString());
                product.setCategory(category);
            }
            product.setName(name);
            product.setDescription("Base product for " + name);
            product.setVariants(handleVariantProduct(variants, category, product));
            products.add(product);
        });
        return products;
    }

    private List<Product> handleVariantProduct(List<MarketingxanhServiceResponse> data, Category category, Product base) {
        if (CollectionUtils.isEmpty(data) || Objects.isNull(category)) {
            return Collections.emptyList();
        }

        List<String> skus = data.stream()
                .map(MarketingxanhServiceResponse::getService)
                .filter(Objects::nonNull)
                .toList();

        Map<String, Product> existingProductMap = productService.getAllBySkus(skus).stream()
                .collect(Collectors.toMap(Product::getSku, Function.identity()));

        return data.stream()
                .filter(response -> Objects.nonNull(response.getRate()) && !BigDecimal.ZERO.equals(response.getRate()))
                .map(response -> createOrUpdateProduct(response, category, existingProductMap, base))
                .toList();
    }

    private Product createOrUpdateProduct(MarketingxanhServiceResponse response,
                                          Category category,
                                          Map<String, Product> existingProductMap, Product base) {
        Product product = existingProductMap.get(response.getService());
        if (Objects.isNull(product)) {
            product = new Product();
            product.setSku(UUID.randomUUID().toString());
            product.setCategory(category);
        }
        product.setName(response.getName());
        product.setOriginPrice(response.getRate().divide(BigDecimal.valueOf(1000), 0, RoundingMode.CEILING));
        product.setDescription(response.getDesc());
        product.setMinOrderQuantity(response.getMin());
        product.setMaxOrderQuantity(response.getMax());
        product.setPrice(response.getRate()
                .divide(BigDecimal.valueOf(1000), 0, RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(10)));
        product.setExternalId(response.getService());
        product.setBase(base);
        return product;
    }

}