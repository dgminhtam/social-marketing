package com.social.marketing.search;

import com.social.marketing.search.resolver.SearchSpecificationResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SearchConfiguration implements WebMvcConfigurer {

    private final SearchSpecificationResolver resolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(resolver);
    }
}
