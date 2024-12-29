package com.social.marketing.auth;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    private SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (securityProperties.isEnabled()) {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers(securityProperties.getPublicEndpoints()).permitAll()
                            .anyRequest().authenticated()
                    )
                    .cors(withDefaults())
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(withDefaults())
                    )
                    .build();
        } else {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authorize) -> authorize
                            .anyRequest().permitAll()
                    )
                    .cors(withDefaults())
                    .build();
        }
    }
}
