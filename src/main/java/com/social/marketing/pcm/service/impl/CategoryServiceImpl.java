package com.social.marketing.pcm.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.request.CreateCategoryRequest;
import com.social.marketing.pcm.model.request.UpdateCategoryRequest;
import com.social.marketing.pcm.model.response.CategoryResponse;
import com.social.marketing.pcm.repository.CategoryRepository;
import com.social.marketing.pcm.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public record CategoryServiceImpl(CategoryRepository categoryRepository,
                                  MediaService mediaService) implements CategoryService {

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
        Specification<Category> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.isNull(root.get(Category.Fields.parent));
        List<Category> categories = categoryRepository.findAll(specification);
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

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        categoryRepository.save(category);
        return convert(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse convert(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setSlug(category.getSlug());
        categoryResponse.setActive(category.isActive());
        categoryResponse.setMedia(mediaService.convert(category.getImage()));
        List<Category> children = category.getChildren();
        categoryResponse.setChildren(children.stream().map(this::convert).toList());
        categoryResponse.setCreatedDate(category.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        categoryResponse.setLastModifiedDate(category.getLastModifiedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        return categoryResponse;
    }

    @Override
    public Category findBySlug(String slug) {
        Optional<Category> categoryOptional = categoryRepository.findBySlug(slug);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }
        return categoryOptional.get();
    }
}
