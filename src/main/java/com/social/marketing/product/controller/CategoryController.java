package com.social.marketing.product.controller;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.model.request.UpdateCategoryRequest;
import com.social.marketing.product.model.response.CategoryResponse;
import com.social.marketing.product.model.response.ClientCategoryResponse;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public Page<CategoryResponse> getCategories(@Search Specification<Category> specification, Pageable pageable) {
        return categoryService.getCategories(specification, pageable);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }
}
