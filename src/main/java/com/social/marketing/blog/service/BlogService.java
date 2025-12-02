package com.social.marketing.blog.service;

import com.social.marketing.blog.model.request.CreateBlogPostRequest;
import com.social.marketing.blog.model.request.UpdateBlogPostRequest;
import com.social.marketing.blog.model.response.BlogPostListResponse;
import com.social.marketing.blog.model.response.BlogPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    Page<BlogPostListResponse> getBlogPosts(Pageable pageable);

    Page<BlogPostListResponse> getVisibleBlogPosts(Pageable pageable);

    BlogPostResponse getBlogPost(Long id);

    BlogPostResponse getBlogPostBySlug(String slug);

    BlogPostResponse createBlogPost(CreateBlogPostRequest request);

    BlogPostResponse updateBlogPost(Long id, UpdateBlogPostRequest request);

    void deleteBlogPost(Long id);
}
