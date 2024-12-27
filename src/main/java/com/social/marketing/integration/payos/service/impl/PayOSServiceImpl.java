package com.social.marketing.integration.payos.service.impl;

import com.social.marketing.integration.payos.configuration.PayOSProperties;
import com.social.marketing.integration.payos.model.request.PayOSRequestPaymentRequest;
import com.social.marketing.integration.payos.model.response.PayOSRequestPaymentResponse;
import com.social.marketing.integration.payos.service.PayOSService;
import com.social.marketing.rest.factory.ResponseTypeFactory;
import com.social.marketing.rest.service.RestTemplateService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class PayOSServiceImpl implements PayOSService {

    private static final String REQUEST_PAYMENT = "/v2/payment-requests";

    @Resource
    private RestTemplateService restTemplateService;

    @Resource
    private PayOSProperties properties;

    protected HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-client-id", properties.getClientId());
        headers.add("x-api-key", properties.getApiKey());
        return headers;
    }

    @Override
    public PayOSRequestPaymentResponse requestPayment(PayOSRequestPaymentRequest request) {
        return restTemplateService.postData(properties.getUrl() + REQUEST_PAYMENT, buildHeaders(), request, ResponseTypeFactory.createFor(PayOSRequestPaymentResponse.class));
    }
}
