package com.social.marketing.blog;

import com.social.marketing.blog.repository.BlogPostRepository;
import com.social.marketing.blog.service.BlogService;
import com.social.marketing.blog.service.impl.BlogServiceImpl;
import com.social.marketing.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BlogConfiguration {

    @Bean
    public BlogService blogService(BlogPostRepository blogPostRepository, MediaService mediaService) {
        return new BlogServiceImpl(blogPostRepository, mediaService);
    }
}
