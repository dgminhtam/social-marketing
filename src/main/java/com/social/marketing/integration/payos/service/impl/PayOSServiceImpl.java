package com.social.marketing.integration.payos.service.impl;

import com.social.marketing.integration.payos.configuration.PayOSProperties;
import com.social.marketing.integration.payos.exception.PayOSException;
import com.social.marketing.integration.payos.model.request.PayOSRequestPaymentRequest;
import com.social.marketing.integration.payos.model.response.PayOSRequestPaymentResponse;
import com.social.marketing.integration.payos.service.PayOSService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class PayOSServiceImpl implements PayOSService {

    @Resource
    private PayOSProperties properties;

    @Resource
    private PayOS payOS;

    @Override
    public PayOSRequestPaymentResponse requestPayment(PayOSRequestPaymentRequest request) {
        PaymentData paymentData = PaymentData.builder()
                .orderCode(request.getOrderCode())
                .amount(request.getAmount())
                .description(request.getDescription())
                .cancelUrl(properties.getCancelUrl())
                .returnUrl(properties.getReturnUrl())
                .expiredAt(ZonedDateTime.now(ZoneId.of(properties.getExpired().getZoneId()))
                        .plusMinutes(properties.getExpired().getMinute())
                        .toEpochSecond())
                .build();
        try {
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            PayOSRequestPaymentResponse response = new PayOSRequestPaymentResponse();
            response.setBin(data.getBin());
            response.setOrderCode(data.getOrderCode());
            response.setAmount(BigDecimal.valueOf(data.getAmount()));
            response.setDescription(data.getDescription());
            response.setCheckoutUrl(data.getCheckoutUrl());
            return response;
        } catch (Exception e) {
            throw new PayOSException("Server thanh toán đang bảo trì, vui lòng thử lại sau.");
        }
    }
}
