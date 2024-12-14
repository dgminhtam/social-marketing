package com.social.marketing;

import com.social.marketing.integration.marketingxanh.configuration.MarketingxanhProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MarketingxanhProperties.class, Auth0Properties.class})
public class ApplicationConfiguration {
}
