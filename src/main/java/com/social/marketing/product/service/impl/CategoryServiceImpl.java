package com.social.marketing.product.service.impl;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.repository.CategoryRepository;
import com.social.marketing.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Override
    public void saveAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }
}
