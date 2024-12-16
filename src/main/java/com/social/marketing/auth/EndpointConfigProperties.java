package com.social.marketing.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@ConfigurationProperties(prefix = "public.endpoints")
@Component
@Getter
@Setter
public class EndpointConfigProperties {

    private String location;

    private List<String> publicEndpoints;

    @PostConstruct
    public void loadPublicEndpoints() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Resource resource = new ClassPathResource(location);
            PublicEndpoints publicEndpoints = objectMapper.readValue(resource.getInputStream(), PublicEndpoints.class);
            this.publicEndpoints = publicEndpoints.getEndpoints();
        } catch (IOException e) {
            throw new RuntimeException("Error loading public endpoints from JSON file", e);
        }
    }

    @Setter
    @Getter
    private static class PublicEndpoints {

        @JsonProperty("publicEndpoints")
        private List<String> endpoints;
    }
}