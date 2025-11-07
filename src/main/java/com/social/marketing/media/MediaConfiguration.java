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

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint; // Endpoint của R2

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region)) // 'auto'
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
    public FileService fileService(Tika tika, MediaProperties properties) {
        return new FileServiceImpl(tika, properties);
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
