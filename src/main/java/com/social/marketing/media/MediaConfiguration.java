package com.social.marketing.media;

import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.respository.MediaRepository;
import com.social.marketing.media.service.FileService;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.media.service.impl.FileServiceImpl;
import com.social.marketing.media.service.impl.MediaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MediaProperties.class)
@RequiredArgsConstructor
public class MediaConfiguration {

    @Bean
    public MediaService mediaService(MediaRepository mediaRepository, FileService fileService, MediaProperties properties) {
        return new MediaServiceImpl(mediaRepository, fileService, properties);
    }

    @Bean
    public FileService fileService(Tika tika) {
        return new FileServiceImpl(tika);
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
