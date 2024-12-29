package com.social.marketing.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private boolean enabled;

    private PublicEndpoint publicEndpoint;

    private String[] publicEndpoints;

    @Data
    public static class PublicEndpoint {

        private String location;

    }

    @PostConstruct
    public void loadPublicEndpoints() {
        try {
            Resource resource = new ClassPathResource(publicEndpoint.getLocation());
            ObjectMapper objectMapper = new ObjectMapper();
            this.publicEndpoints = objectMapper.readValue(resource.getInputStream(), String[].class);
        } catch (IOException e) {
            throw new RuntimeException("Error loading public endpoints from JSON file", e);
        }
    }
}