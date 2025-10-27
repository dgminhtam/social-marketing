package com.social.marketing.initialize.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "init")
@Getter
@Setter
public class InitializeProperties {

    private String managementAudience;
}
