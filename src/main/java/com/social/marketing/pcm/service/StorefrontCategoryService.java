package com.social.marketing.pcm.service;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.response.ClientCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public interface StorefrontCategoryService {

    void saveAll(List<Category> categories);

    List<Category> getAllByNames(Set<String> names);

    Page<ClientCategoryResponse> getCategories(Specification<Category> specification, Pageable pageable);

    void save(Category rootCategory);

    List<ClientCategoryResponse> getCategoryTree();

    ClientCategoryResponse convert(Category category);
}

