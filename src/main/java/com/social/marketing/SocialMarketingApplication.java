package com.social.marketing;

import com.social.marketing.marketingxanh.configuration.MarketingxanhProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MarketingxanhProperties.class)
public class SocialMarketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMarketingApplication.class, args);
    }

}
