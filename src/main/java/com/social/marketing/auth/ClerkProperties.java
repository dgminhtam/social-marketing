package com.social.marketing.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "clerk")
@Getter
@Setter
public class ClerkProperties {

    private List<String> issuers;

    private Webhook webhook;

    @Getter
    @Setter
    public static final class Webhook {

        private String secret;
    }
}


