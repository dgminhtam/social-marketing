package com.social.marketing.product.initial;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhResponse;
import com.social.marketing.integration.marketingxanh.service.MarketingxanhService;
import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        List<MarketingxanhResponse> responses = marketingxanhService.getServices();
        if (responses.isEmpty()) {
            throw new NotFoundException("No marketingxanh services found");
        }

        Map<String, Map<String, List<MarketingxanhResponse>>> rootCategoryMap = responses.stream()
                .collect(Collectors.groupingBy(
                        response -> response.getCategory().split("\\|")[1].toLowerCase().trim(),
                        Collectors.groupingBy(
                                response -> response.getCategory().split("\\|")[0].trim()
                        )
                ));

        List<Category> rootCategories = handleCategory(rootCategoryMap.keySet(), null);
        rootCategories.forEach(rootCategory -> {
            Map<String, List<MarketingxanhResponse>> categoryResponse = rootCategoryMap.get(rootCategory.getName());
            List<Category> categories = handleCategory(categoryResponse.keySet(), rootCategory);
            categories.forEach(category -> handleProduct(categoryResponse.get(category.getName()), category));
        });
    }

    private List<Category> handleCategory(Set<String> names, Category parent) {
        Map<String, Category> existingCategoryMap = categoryService.getAllByNames(names)
                .stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));
        List<Category> categories = new ArrayList<>();
        names.forEach(name -> {
            if (!existingCategoryMap.containsKey(name)) {
                Category category = new Category();
                category.setName(name);
                if (Objects.nonNull(parent)) {
                    category.setParent(parent);
                }
                categories.add(category);
            }
        });
        categoryService.saveAll(categories);
        return categories;
    }

    private void handleProduct(List<MarketingxanhResponse> source, Category category) {
        List<String> productIds = source.stream().map(MarketingxanhResponse::getService).toList();
        Map<String, Product> existingProductMap = productService.getAllBySkus(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getSku, Function.identity()));
        List<Product> products = new ArrayList<>();
        source.forEach(marketingxanhResponse -> {
            Product product = existingProductMap.get(marketingxanhResponse.getService());
            if (existingProductMap.containsKey(product.getSku())) {
                product = new Product();
                product.setSku(marketingxanhResponse.getService());
            }
            product.setCategory(category);
            product.setName(marketingxanhResponse.getName());
            product.setPrice(marketingxanhResponse.getRate());
            product.setDescription(marketingxanhResponse.getDesc());
            products.add(product);
        });
        productService.saveAll(products);
    }
}