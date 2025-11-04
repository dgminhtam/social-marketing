package com.social.marketing.pcm.service;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.request.CreateCategoryRequest;
import com.social.marketing.pcm.model.request.UpdateCategoryRequest;
import com.social.marketing.pcm.model.response.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    Category getCategoryById(Long id);

    void saveAll(List<Category> categories);

    List<Category> getAllByNames(Set<String> names);

    Page<CategoryResponse> getCategories(Specification<Category> specification, Pageable pageable);

    void save(Category rootCategory);

    List<CategoryResponse> getCategoryTree();

    CategoryResponse getCategory(Long id);

    CategoryResponse updateCategory(Long id, @Valid UpdateCategoryRequest request);

    CategoryResponse createCategory(CreateCategoryRequest request);

    void deleteCategory(Long id);

    CategoryResponse convert(Category category);

    Category findByCode(String categoryCode);
}

