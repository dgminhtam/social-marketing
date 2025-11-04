package com.social.marketing.pcm.controller;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.request.CreateCategoryRequest;
import com.social.marketing.pcm.model.request.UpdateCategoryRequest;
import com.social.marketing.pcm.model.response.CategoryResponse;
import com.social.marketing.pcm.service.CategoryService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public CategoryResponse createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
