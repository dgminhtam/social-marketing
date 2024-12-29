package com.social.marketing.integration.payos.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payos")
@Getter
@Setter
public class PayOSProperties {

    private String url;

    private String clientId;

    private String apiKey;

    private String checksumKey;

    private String cancelUrl;

    private String returnUrl;

    private long expiredAt;
}
