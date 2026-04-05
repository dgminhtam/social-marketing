package com.social.marketing.pcm.service.impl;

import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.response.StorefrontCategoryResponse;
import com.social.marketing.pcm.repository.CategoryRepository;
import com.social.marketing.pcm.service.StorefrontCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorefrontCategoryServiceImpl implements StorefrontCategoryService {

    private final CategoryRepository categoryRepository;

    private final MediaService mediaService;

    @Override
    public void saveAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> getAllByNames(Set<String> names) {
        Specification<Category> specification = (root, query, builder) -> builder.in(root.get(Category.Fields.name))
                .value(names);
        return categoryRepository.findAll(specification);
    }

    @Override
    public Page<StorefrontCategoryResponse> getCategories(Specification<Category> specification, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(specification, pageable);
        List<StorefrontCategoryResponse> storefrontCategoryRespons = categories.getContent().stream().map(this::convert)
                .toList();
        return new PageImpl<>(storefrontCategoryRespons, categories.getPageable(), categories.getTotalElements());
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<StorefrontCategoryResponse> getCategoryTree() {
        Specification<Category> specification = (root, query, criteriaBuilder) -> criteriaBuilder
                .isNull(root.get(Category.Fields.parent));
        List<Category> categories = categoryRepository.findAll(specification);
        return categories.stream().map(this::convert).toList();
    }

    @Override
    public StorefrontCategoryResponse convert(Category category) {
        StorefrontCategoryResponse categoryResponse = new StorefrontCategoryResponse();
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
    public List<StorefrontCategoryResponse> getRootCategories() {
        Specification<Category> specification = (root, query, criteriaBuilder) -> criteriaBuilder
                .isNull(root.get(Category.Fields.parent));
        List<Category> categories = categoryRepository.findAll(specification);
        return categories.stream().map(this::convert).toList();
    }
}
