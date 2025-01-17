package com.social.marketing.integration.payos.configuration;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfiguration {

    @Resource
    private PayOSProperties payOSProperties;

    @Bean
    public PayOS payOS() {
        return new PayOS(payOSProperties.getClientId(), payOSProperties.getApiKey(), payOSProperties.getChecksumKey());
    }
}
