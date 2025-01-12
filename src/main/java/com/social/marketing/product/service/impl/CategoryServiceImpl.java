package com.social.marketing.product.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.product.entity.Category;
import com.social.marketing.product.model.request.UpdateCategoryRequest;
import com.social.marketing.product.model.response.CategoryResponse;
import com.social.marketing.product.repository.CategoryRepository;
import com.social.marketing.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }
        return categoryOpt.get();
    }

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
    public Page<CategoryResponse> getCategories(Specification<Category> specification, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(specification, pageable);
        List<CategoryResponse> categoryResponses = categories.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(categoryResponses, categories.getPageable(), categories.getTotalElements());
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convert).toList();
    }

    @Override
    public CategoryResponse getCategory(Long id) {
        Category category = getCategoryById(id);
        return convert(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = getCategoryById(id);
        category.setName(request.name());
        category.setDescription(request.description());
        categoryRepository.save(category);
        return convert(category);
    }

    private CategoryResponse convert(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        List<Category> children = category.getChildren();
        categoryResponse.setChildren(children.stream().map(this::convert).toList());
        return categoryResponse;
    }
}
