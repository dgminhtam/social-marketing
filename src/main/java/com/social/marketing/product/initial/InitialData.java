package com.social.marketing.product.initial;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.marketingxanh.model.response.MarketingxanhResponse;
import com.social.marketing.marketingxanh.service.MarketingxanhService;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            System.out.println("No data received from MarketingService");
            return;
        }

        Map<String, Map<String, List<MarketingxanhResponse>>> rootCategoryMap = responses.stream()
                .collect(Collectors.groupingBy(
                        response -> response.getCategory().split("\\|")[1].toLowerCase().trim(),
                        Collectors.groupingBy(
                                response -> response.getCategory().split("\\|")[0].trim()
                        )
                ));

        List<Category> rootCategories = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        rootCategoryMap.keySet().forEach(rootCategoryName -> {
            Category rootCategory = new Category();
            rootCategory.setName(rootCategoryName);
            rootCategories.add(rootCategory);
            Map<String, List<MarketingxanhResponse>> categoryMap = rootCategoryMap.get(rootCategoryName);
            categoryMap.keySet().forEach(categoryName -> {
                Category category = new Category();
                category.setName(categoryName);
                category.setParent(rootCategory);
                categories.add(category);
                List<MarketingxanhResponse> marketingRespons = categoryMap.get(categoryName);
                marketingRespons.forEach(marketingxanhResponse -> {
                    Product product = new Product();
                    product.setSku(marketingxanhResponse.getService());
                    product.setCategory(category);
                    product.setName(marketingxanhResponse.getName());
                    product.setPrice(marketingxanhResponse.getRate());
                    product.setDescription(marketingxanhResponse.getDesc());
                    products.add(product);
                });

            });
        });
        categoryService.saveAll(rootCategories);
        categoryService.saveAll(categories);
        productService.saveAll(products);
    }
}