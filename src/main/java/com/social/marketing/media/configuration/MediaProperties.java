package com.social.marketing.media.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "file")
public class MediaProperties {

    private String uploadDir;

    private Long maxSize;

    private List<String> acceptMimeTypes;
}
