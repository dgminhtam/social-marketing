package com.social.marketing.product.controller;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.model.response.ClientCategoryResponse;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client/categories")
public class ClientCategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/tree")
    public List<ClientCategoryResponse> getCategories() {
        return categoryService.getCategoryTree();
    }

    @GetMapping
    public Page<ClientCategoryResponse> getCategories(@Search Specification<Category> specification, Pageable pageable) {
        return categoryService.getCategories(specification, pageable);
    }
}
