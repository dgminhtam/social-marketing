package com.social.marketing.media;

import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.respository.MediaRepository;
import com.social.marketing.media.service.FileService;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.media.service.impl.FileServiceImpl;
import com.social.marketing.media.service.impl.MediaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(MediaProperties.class)
@RequiredArgsConstructor
public class MediaConfiguration {

    @Value("${application.cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${application.cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${application.cloudflare.r2.endpoint}")
    private String endpoint; // Endpoint của R2

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of("auto")) // 'auto'
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                // Dòng quan trọng nhất: Chỉ định endpoint là R2
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    @Bean
    public MediaService mediaService(MediaRepository mediaRepository, FileService fileService, MediaProperties properties) {
        return new MediaServiceImpl(mediaRepository, fileService, properties);
    }

    @Bean
    public FileService fileService(Tika tika, MediaProperties properties, S3Client s3Client) {
        return new FileServiceImpl(tika, properties, s3Client);
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
