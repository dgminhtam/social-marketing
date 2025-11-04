package com.social.marketing.pcm.service.impl;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.response.ClientCategoryResponse;
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
    public Page<ClientCategoryResponse> getCategories(Specification<Category> specification, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(specification, pageable);
        List<ClientCategoryResponse> clientCategoryResponses = categories.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(clientCategoryResponses, categories.getPageable(), categories.getTotalElements());
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<ClientCategoryResponse> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convert).toList();
    }

    @Override
    public ClientCategoryResponse convert(Category category) {
        ClientCategoryResponse clientCategoryResponse = new ClientCategoryResponse();
        clientCategoryResponse.setId(category.getId());
        clientCategoryResponse.setName(category.getName());
        clientCategoryResponse.setDescription(category.getDescription());
        List<Category> children = category.getChildren();
        clientCategoryResponse.setChildren(children.stream().map(this::convert).toList());
        return clientCategoryResponse;
    }
}
