package com.social.marketing.pcm.controller;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.response.StorefrontCategoryResponse;
import com.social.marketing.pcm.service.StorefrontCategoryService;
import com.social.marketing.search.anotation.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storefront/categories")
@RequiredArgsConstructor
public class StorefrontCategoryController {

    private final StorefrontCategoryService storefrontCategoryService;

    @GetMapping("/tree")
    public List<StorefrontCategoryResponse> getCategories() {
        return storefrontCategoryService.getCategoryTree();
    }

    @GetMapping
    public Page<StorefrontCategoryResponse> getCategories(@Search Specification<Category> specification, Pageable pageable) {
        return storefrontCategoryService.getCategories(specification, pageable);
    }

    @GetMapping("/root")
    public List<StorefrontCategoryResponse> getRootCategories() {
        return storefrontCategoryService.getRootCategories();
    }
}
