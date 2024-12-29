package com.social.marketing.integration.payos.aspect;

import com.social.marketing.integration.payos.configuration.PayOSProperties;
import com.social.marketing.integration.payos.model.request.PayOSRequestPaymentRequest;
import com.social.marketing.integration.payos.service.PayOSTransactionService;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class PayOSAspect {

    @Resource
    private PayOSTransactionService payOSTransactionService;

    @Resource
    private PayOSProperties properties;

    @Before("execution(* com.social.marketing.integration.payos.service.impl.PayOSServiceImpl.*(..)) && args(request,..)")
    public void addAuthInfo(JoinPoint joinPoint, PayOSRequestPaymentRequest request) {
        Map<String, String> signatureMap = new HashMap<>();
        signatureMap.put("amount", String.valueOf(request.getAmount()));
        signatureMap.put("cancelUrl", properties.getCancelUrl());
        signatureMap.put("description", request.getDescription());
        signatureMap.put("orderCode", request.getOrderCode().toString());
        signatureMap.put("returnUrl", properties.getReturnUrl());
        request.setCancelUrl(properties.getCancelUrl());
        request.setReturnUrl(properties.getReturnUrl());
        request.setExpiredAt(LocalDateTime.now().plusMinutes(properties.getExpiredAt()).toEpochSecond(ZoneOffset.UTC));
        request.setSignature(payOSTransactionService.generateSignature(signatureMap));
    }

}
