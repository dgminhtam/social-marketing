package com.social.marketing.product.initial;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhServiceResponse;
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
        List<MarketingxanhServiceResponse> responses = marketingxanhService.getServices();
        if (responses.isEmpty()) {
            throw new NotFoundException("No marketingxanh services found");
        }
        Map<String, List<MarketingxanhServiceResponse>> categoryMap = responses.stream()
                .collect(Collectors.groupingBy(MarketingxanhServiceResponse::getCategory));

        List<Category> categories = handleCategory(categoryMap.keySet());
        categories.forEach(category -> handleProduct(categoryMap.get(category.getName()), category));
    }

    private List<Category> handleCategory(Set<String> names) {
        Map<String, Category> existingCategoryMap = categoryService.getAllByNames(names)
                .stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));
        List<Category> categories = new ArrayList<>();
        names.forEach(name -> {
            if (!existingCategoryMap.containsKey(name)) {
                Category category = new Category();
                category.setName(name);
                categories.add(category);
            }
        });

        return categoryService.saveAll(categories);
    }

    private void handleProduct(List<MarketingxanhServiceResponse> source, Category category) {
        List<String> skus = source.stream().map(MarketingxanhServiceResponse::getService).toList();
        Map<String, Product> existingProductMap = productService.getAllBySkus(skus)
                .stream()
                .collect(Collectors.toMap(Product::getSku, Function.identity()));
        List<Product> products = new ArrayList<>();
        source.forEach(marketingxanhServiceResponse -> {
            Product product = existingProductMap.get(marketingxanhServiceResponse.getService());
            if (Objects.isNull(product)) {
                product = new Product();
                product.setSku(marketingxanhServiceResponse.getService());
            }
            product.setCategory(category);
            product.setName(marketingxanhServiceResponse.getName());
            product.setPrice(marketingxanhServiceResponse.getRate());
            product.setDescription(marketingxanhServiceResponse.getDesc());
            products.add(product);
        });
        productService.saveAll(products);
    }
}