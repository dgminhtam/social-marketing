package com.social.marketing.product.service;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.model.response.ClientCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    void saveAll(List<Category> categories);

    List<Category> getAllByNames(Set<String> names);

    Page<ClientCategoryResponse> getCategories(Specification<Category> specification, Pageable pageable);

    void save(Category rootCategory);

    List<ClientCategoryResponse> getCategoryTree();
}

