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

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            throw new RuntimeException("No marketingxanh services found.");
        }
        Map<String, Map<String, List<MarketingxanhServiceResponse>>> data = responses.stream()
                .collect(Collectors.groupingBy(
                        response -> response.getCategory().split("\\|")[1].toUpperCase().trim(),
                        Collectors.groupingBy(MarketingxanhServiceResponse::getCategory)));

        Map<String, Category> rootCategoriesExited = categoryService.getAllByNames(data.keySet())
                .stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));
        data.forEach((rootCategoryName, categoryMap) -> {
            Category rootCategory = rootCategoriesExited.get(rootCategoryName);
            if (Objects.isNull(rootCategory)) {
                rootCategory = new Category();
                rootCategory.setName(rootCategoryName);
                rootCategory.setDescription("View all services of " + rootCategoryName);
                categoryService.save(rootCategory);
                handleCategory(categoryMap, rootCategory);
            }
        });
    }

    private void handleCategory(Map<String, List<MarketingxanhServiceResponse>> categoryMap,
                                Category rootCategory) {
        Map<String, Category> existingCategoryMap = categoryService.getAllByNames(categoryMap.keySet())
                .stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));
        categoryMap.forEach((categoryName, productsResponse) -> {
            if (!existingCategoryMap.containsKey(categoryName)) {
                Category category = new Category();
                category.setName(categoryName);
                category.setDescription(categoryName);
                category.setParent(rootCategory);
                categoryService.save(category);
                handleProduct(productsResponse, category);
            }
        });

    }

    private void handleProduct(List<MarketingxanhServiceResponse> source, Category category) {
        if (CollectionUtils.isEmpty(source) || Objects.isNull(category)) {
            return;
        }

        List<String> skus = source.stream()
                .map(MarketingxanhServiceResponse::getService)
                .filter(Objects::nonNull)
                .toList();

        Map<String, Product> existingProductMap = productService.getAllBySkus(skus).stream()
                .collect(Collectors.toMap(Product::getSku, Function.identity()));

        List<Product> products = source.stream()
                .map(response -> createOrUpdateProduct(response, category, existingProductMap))
                .toList();

        productService.saveAll(products);
    }

    private Product createOrUpdateProduct(MarketingxanhServiceResponse response,
                                          Category category,
                                          Map<String, Product> existingProductMap) {
        Product product = existingProductMap.get(response.getService());
        if (Objects.isNull(product)) {
            product = new Product();
            product.setSku(response.getService());
            product.setCategory(category);
        }
        product.setName(response.getName());
        product.setOriginPrice(response.getRate());
        product.setDescription(response.getDesc());
        product.setMinOrderQuantity(response.getMin());
        product.setMaxOrderQuantity(response.getMax());
        return product;
    }

}