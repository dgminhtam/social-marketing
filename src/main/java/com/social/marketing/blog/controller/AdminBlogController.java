package com.social.marketing.blog.controller;

import com.social.marketing.blog.model.request.CreateBlogPostRequest;
import com.social.marketing.blog.model.request.UpdateBlogPostRequest;
import com.social.marketing.blog.model.response.BlogPostListResponse;
import com.social.marketing.blog.model.response.BlogPostResponse;
import com.social.marketing.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

    private final BlogService blogService;

    @GetMapping
    public Page<BlogPostListResponse> getBlogPosts(Pageable pageable) {
        return blogService.getBlogPosts(pageable);
    }

    @GetMapping("/{id}")
    public BlogPostResponse getBlogPost(@PathVariable Long id) {
        return blogService.getBlogPost(id);
    }

    @PostMapping
    public BlogPostResponse createBlogPost(@RequestBody @Valid CreateBlogPostRequest request) {
        return blogService.createBlogPost(request);
    }

    @PutMapping("/{id}")
    public BlogPostResponse updateBlogPost(@PathVariable Long id, @RequestBody @Valid UpdateBlogPostRequest request) {
        return blogService.updateBlogPost(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBlogPost(@PathVariable Long id) {
        blogService.deleteBlogPost(id);
    }
}
