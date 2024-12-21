package com.social.marketing.integration.marketingxanh.service.impl;

import com.social.marketing.integration.marketingxanh.configuration.MarketingxanhProperties;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhOrderResponse;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhServiceResponse;
import com.social.marketing.integration.marketingxanh.service.MarketingxanhService;
import com.social.marketing.rest.factory.ResponseTypeFactory;
import com.social.marketing.rest.service.RestTemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketingxanhServiceImpl implements MarketingxanhService {

    @Resource
    private RestTemplateService restTemplateService;

    @Resource
    private MarketingxanhProperties properties;

    @Override
    public List<MarketingxanhServiceResponse> getServices() {
        String url = String.format("%s?key=%s&action=services", properties.getUrl(), properties.getKey());
        return restTemplateService.getData(url, null, ResponseTypeFactory.createForListOf(MarketingxanhServiceResponse.class));
    }

    @Override
    public List<MarketingxanhOrderResponse> getOrder(String orderId) {
        String url = String.format("%s?key=%s&action=services&order=%s", properties.getUrl(), properties.getKey(), orderId);
        return restTemplateService.getData(url, null, ResponseTypeFactory.createForListOf(MarketingxanhOrderResponse.class));
    }

    @Override
    public List<MarketingxanhOrderResponse> getOrders(List<String> orderIds) {
        String url = String.format("%s?key=%s&action=services&orders=%s", properties.getUrl(), properties.getKey(), String.join(",", orderIds));
        return restTemplateService.getData(url, null, ResponseTypeFactory.createForListOf(MarketingxanhOrderResponse.class));
    }
}