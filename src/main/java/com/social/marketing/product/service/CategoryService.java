package com.social.marketing.product.service;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.model.response.CategoryResponse;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    void saveAll(List<Category> categories);

    List<Category> getAllByNames(Set<String> names);

    List<Category> getRootCategory();

    List<CategoryResponse> getCategoryTree();
}

