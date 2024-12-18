package com.social.marketing.product.controller;

import com.social.marketing.product.model.response.CategoryResponse;
import com.social.marketing.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/tree")
    public List<CategoryResponse> getCategoryTree() {
        return categoryService.getCategoryTree();
    }
}
