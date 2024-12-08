package com.social.marketing.marketingxanh.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.marketingxanh.configuration.MarketingxanhProperties;
import com.social.marketing.marketingxanh.model.response.MarketingxanhResponse;
import com.social.marketing.marketingxanh.service.MarketingxanhService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MarketingxanhServiceImpl implements MarketingxanhService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private MarketingxanhProperties properties;

    @Override
    public List<MarketingxanhResponse> getServices() {
        String url = String.format("%s?key=%s&action=services", properties.getUrl(), properties.getKey());
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse data from API", e);
        }
    }
}