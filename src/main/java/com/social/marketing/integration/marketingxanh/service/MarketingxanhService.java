package com.social.marketing.integration.marketingxanh.service;

import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhOrderResponse;
import com.social.marketing.integration.marketingxanh.model.response.MarketingxanhServiceResponse;

import java.util.List;

public interface MarketingxanhService {

    List<MarketingxanhServiceResponse> getServices();

    List<MarketingxanhOrderResponse> getOrder(String orderId);

    List<MarketingxanhOrderResponse> getOrders(List<String> orderIds);
}
