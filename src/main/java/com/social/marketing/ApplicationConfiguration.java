package com.social.marketing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.social.marketing.auth.SecurityConfiguration;
import com.social.marketing.media.MediaConfiguration;
import com.social.marketing.pcm.PcmConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(
        {
                SecurityConfiguration.class,
                PcmConfiguration.class,
                MediaConfiguration.class
        }
)
public class ApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper;
    }
}
