package com.social.marketing;

import com.social.marketing.auth.configuration.AuthProperties;
import com.social.marketing.marketingxanh.configuration.MarketingxanhProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class, MarketingxanhProperties.class})
public class ApplicationConfiguration {
}
