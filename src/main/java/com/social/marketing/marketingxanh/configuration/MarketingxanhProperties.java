package com.social.marketing.marketingxanh.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "marketingxanh")
@Getter
@Setter
public class MarketingxanhProperties {

    private String url;

    private String key;
}
