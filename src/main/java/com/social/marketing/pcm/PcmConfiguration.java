package com.social.marketing.pcm;

import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.repository.CategoryRepository;
import com.social.marketing.pcm.repository.ProductRepository;
import com.social.marketing.pcm.service.CategoryService;
import com.social.marketing.pcm.service.ProductService;
import com.social.marketing.pcm.service.impl.CategoryServiceImpl;
import com.social.marketing.pcm.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PcmConfiguration {

    @Bean
    public CategoryService categoryService(CategoryRepository categoryRepository, ProductRepository productRepository, MediaService mediaService) {
        return new CategoryServiceImpl(categoryRepository, productRepository, mediaService);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository, MediaService mediaService, CategoryService categoryService) {
        return new ProductServiceImpl(productRepository, mediaService, categoryService);
    }


}
