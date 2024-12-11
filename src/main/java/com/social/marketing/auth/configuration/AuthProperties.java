package com.social.marketing.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private JWT jwt;

    @Getter
    @Setter
    public static class JWT {

        private String SecretKey;

        private Long expiration;

        private RefreshToken refreshToken;

        @Getter
        @Setter
        public static class RefreshToken {

            private Long expiration;
        }
    }
}

