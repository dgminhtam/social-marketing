package com.social.marketing.blog.controller;

import com.social.marketing.blog.model.response.BlogPostListResponse;
import com.social.marketing.blog.model.response.BlogPostResponse;
import com.social.marketing.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storefront/blogs")
@RequiredArgsConstructor
public class StorefrontBlogController {

    private final BlogService blogService;

    @GetMapping
    public Page<BlogPostListResponse> getVisibleBlogPosts(Pageable pageable) {
        return blogService.getVisibleBlogPosts(pageable);
    }

    @GetMapping("/{slug}")
    public BlogPostResponse getBlogPostBySlug(@PathVariable String slug) {
        return blogService.getBlogPostBySlug(slug);
    }
}
