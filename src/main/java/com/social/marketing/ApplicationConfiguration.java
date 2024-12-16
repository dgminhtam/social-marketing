package com.social.marketing;

import com.social.marketing.auth.EndpointConfigProperties;
import com.social.marketing.integration.marketingxanh.configuration.MarketingxanhProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MarketingxanhProperties.class, EndpointConfigProperties.class})
public class ApplicationConfiguration {
}
