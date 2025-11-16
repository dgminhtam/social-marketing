package com.social.marketing.pcm.service.impl;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.request.CreateCategoryRequest;
import com.social.marketing.pcm.model.request.UpdateCategoryRequest;
import com.social.marketing.pcm.model.response.CategoryResponse;
import com.social.marketing.pcm.repository.CategoryRepository;
import com.social.marketing.pcm.repository.ProductRepository;
import com.social.marketing.pcm.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MediaService mediaService;

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }
        return categoryOpt.get();
    }

    @Override
    public List<Category> getCategoryByIds(List<Long> ids) {
        return categoryRepository.findAllById(ids);
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
    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = getCategoryById(id);
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setActive(request.active());
        category.setDescription(request.description());
        if (request.parentId() != null) {
            Category parent = getCategoryById(request.parentId());
            validateParent(category, parent);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            category.setImage(image);
        }
        return convert(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setActive(request.active());
        category.setDescription(request.description());
        if (request.parentId() != null) {
            Category parent = getCategoryById(request.parentId());
            validateParent(category, parent);
            category.setParent(parent);
        }

        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            category.setImage(image);
        }
        categoryRepository.save(category);
        return convert(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (hasProduct(category)) {
            throw new BadRequestException(
                    "Không thể xóa danh mục '" + category.getName() + "' vì vẫn còn sản phẩm được gán."
            );
        }
        List<Category> children = category.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Category child : children) {
                child.setParent(null);
            }
            categoryRepository.saveAll(children);
        }
        categoryRepository.delete(category);
    }

    private boolean hasProduct(Category category) {
        return productRepository.existsByCategories(Collections.singletonList(category));
    }

    @Override
    public CategoryResponse convert(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setSlug(category.getSlug());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setActive(category.isActive());
        categoryResponse.setImage(mediaService.convert(category.getImage()));
        categoryResponse.setParentId(category.getParent() == null ? null : category.getParent().getId());
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

    private void validateParent(Category category, Category parent) {
        if (category.getId() != null && category.getId().equals(parent.getId())) {
            throw new IllegalArgumentException("Category cannot be its own parent.");
        }

        Category ancestor = parent.getParent();
        while (ancestor != null) {
            if (category.getId() != null && category.getId().equals(ancestor.getId())) {
                throw new IllegalArgumentException("Circular parent relationship detected.");
            }
            ancestor = ancestor.getParent();
        }
    }

}
