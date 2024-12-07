package com.social.marketing.marketing.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.entity.Category;
import com.social.marketing.marketing.model.response.ServiceResponse;
import com.social.marketing.marketing.service.MarketingService;
import com.social.marketing.repository.CategoryRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MarketingServiceImpl implements MarketingService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CategoryRepository categoryRepository;

    private static final String API_URL = "https://marketingxanh.com/api/v1";
    private static final String API_KEY = "j5P2jlUAtLdcuzZL6zr9zLuGzJQdIR4J";
    private static final String ACTION = "services";

    @Override
    public List<ServiceResponse> fetchData() {
        String url = String.format("%s?key=%s&action=%s", API_URL, API_KEY, ACTION);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            List<ServiceResponse> responses = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });
            populate(responses);
            return responses;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse data from API", e);
        }
    }

    private void populate(List<ServiceResponse> responses) {
        Set<String> categoriesStr = new HashSet<>();
        responses.forEach(response -> {
            String categoryStr = response.getCategory();
            String[] cateAndType = categoryStr.split("\\|");
            categoriesStr.add(cateAndType[1].trim());
        });
        List<Category> categories = new ArrayList<>();
        categoriesStr.forEach(categoryStr -> {
            Category category = new Category();
            category.setName(categoryStr);
            categories.add(category);
        });
        categoryRepository.saveAll(categories);
    }
}