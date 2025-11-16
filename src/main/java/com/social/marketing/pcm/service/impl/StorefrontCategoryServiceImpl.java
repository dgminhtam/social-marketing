package com.social.marketing.pcm.service.impl;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.response.StorefrontCategoryResponse;
import com.social.marketing.pcm.repository.CategoryRepository;
import com.social.marketing.pcm.service.StorefrontCategoryService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StorefrontCategoryServiceImpl implements StorefrontCategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Override
    public void saveAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> getAllByNames(Set<String> names) {
        Specification<Category> specification = (root, query, builder) ->
                builder.in(root.get(Category.Fields.name)).value(names);
        return categoryRepository.findAll(specification);
    }

    @Override
    public Page<StorefrontCategoryResponse> getCategories(Specification<Category> specification, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(specification, pageable);
        List<StorefrontCategoryResponse> storefrontCategoryRespons = categories.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(storefrontCategoryRespons, categories.getPageable(), categories.getTotalElements());
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<StorefrontCategoryResponse> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convert).toList();
    }

    @Override
    public StorefrontCategoryResponse convert(Category category) {
        StorefrontCategoryResponse storefrontCategoryResponse = new StorefrontCategoryResponse();
        storefrontCategoryResponse.setId(category.getId());
        storefrontCategoryResponse.setName(category.getName());
        storefrontCategoryResponse.setDescription(category.getDescription());
        List<Category> children = category.getChildren();
        storefrontCategoryResponse.setChildren(children.stream().map(this::convert).toList());
        return storefrontCategoryResponse;
    }
}
