package com.social.marketing.product.service.impl;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.model.response.CategoryResponse;
import com.social.marketing.product.repository.CategoryRepository;
import com.social.marketing.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Override
    public void saveAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> getAllByNames(Set<String> names) {
        Specification<Category> specification =
                (root, query, builder) -> builder.in(root.get(Category.Fields.name)).value(names);
        return categoryRepository.findAll(specification);
    }

    @Override
    public List<Category> getRootCategory() {
        Specification<Category> specification =
                (root, query, builder) -> builder.isEmpty(root.get(Category.Fields.parent));
        return categoryRepository.findAll(specification);
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        List<Category> rootCategories = getRootCategory();
        return convertTree(rootCategories);
    }

    private List<CategoryResponse> convertTree(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        return categories.stream()
                .map(category -> {
                    CategoryResponse categoryResponse = new CategoryResponse();
                    categoryResponse.setName(category.getName());
                    categoryResponse.setChildren(convertTree(category.getChildren().stream().toList()));
                    return categoryResponse;
                }).toList();
    }
}
