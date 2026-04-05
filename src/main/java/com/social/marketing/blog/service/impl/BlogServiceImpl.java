package com.social.marketing.blog.service.impl;

import org.springframework.stereotype.Service;
import com.social.marketing.blog.entity.BlogPost;
import com.social.marketing.blog.model.request.CreateBlogPostRequest;
import com.social.marketing.blog.model.request.UpdateBlogPostRequest;
import com.social.marketing.blog.model.response.BlogPostListResponse;
import com.social.marketing.blog.model.response.BlogPostResponse;
import com.social.marketing.blog.repository.BlogPostRepository;
import com.social.marketing.blog.service.BlogService;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogPostRepository blogPostRepository;
    private final MediaService mediaService;

    @Override
    public Page<BlogPostListResponse> getBlogPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable).map(this::convertToListResponse);
    }

    @Override
    public Page<BlogPostListResponse> getVisibleBlogPosts(Pageable pageable) {
        return blogPostRepository.findByIsVisibleTrue(pageable).map(this::convertToListResponse);
    }

    @Override
    public BlogPostResponse getBlogPost(Long id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog post not found"));
        return convertToResponse(blogPost);
    }

    @Override
    public BlogPostResponse getBlogPostBySlug(String slug) {
        BlogPost blogPost = blogPostRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Blog post not found"));
        return convertToResponse(blogPost);
    }

    @Override
    @Transactional
    public BlogPostResponse createBlogPost(CreateBlogPostRequest request) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(request.getTitle());
        blogPost.setSlug(request.getSlug());
        blogPost.setContent(request.getContent());
        blogPost.setShortDescription(request.getShortDescription());
        blogPost.setVisible(request.isVisible());

        if (request.isVisible()) {
            blogPost.setPublishedAt(LocalDateTime.now());
        }

        if (request.getThumbnailId() != null) {
            Media thumbnail = mediaService.get(request.getThumbnailId());
            blogPost.setThumbnail(thumbnail);
        }

        blogPost = blogPostRepository.save(blogPost);
        return convertToResponse(blogPost);
    }

    @Override
    @Transactional
    public BlogPostResponse updateBlogPost(Long id, UpdateBlogPostRequest request) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog post not found"));

        if (request.getTitle() != null)
            blogPost.setTitle(request.getTitle());
        if (request.getSlug() != null)
            blogPost.setSlug(request.getSlug());
        if (request.getContent() != null)
            blogPost.setContent(request.getContent());
        if (request.getShortDescription() != null)
            blogPost.setShortDescription(request.getShortDescription());

        if (request.getIsVisible() != null) {
            boolean wasVisible = blogPost.isVisible();
            blogPost.setVisible(request.getIsVisible());
            if (!wasVisible && request.getIsVisible() && blogPost.getPublishedAt() == null) {
                blogPost.setPublishedAt(LocalDateTime.now());
            }
        }

        if (request.getThumbnailId() != null) {
            Media thumbnail = mediaService.get(request.getThumbnailId());
            blogPost.setThumbnail(thumbnail);
        }

        blogPost = blogPostRepository.save(blogPost);
        return convertToResponse(blogPost);
    }

    @Override
    @Transactional
    public void deleteBlogPost(Long id) {
        if (!blogPostRepository.existsById(id)) {
            throw new NotFoundException("Blog post not found");
        }
        blogPostRepository.deleteById(id);
    }

    private BlogPostResponse convertToResponse(BlogPost blogPost) {
        BlogPostResponse response = new BlogPostResponse();
        response.setId(blogPost.getId());
        response.setTitle(blogPost.getTitle());
        response.setSlug(blogPost.getSlug());
        response.setContent(blogPost.getContent());
        response.setShortDescription(blogPost.getShortDescription());
        response.setVisible(blogPost.isVisible());
        response.setPublishedAt(blogPost.getPublishedAt());
        response.setCreatedDate(blogPost.getCreatedDate() != null ? blogPost.getCreatedDate().toLocalDateTime() : null);
        response.setLastModifiedDate(
                blogPost.getLastModifiedDate() != null ? blogPost.getLastModifiedDate().toLocalDateTime() : null);

        if (blogPost.getThumbnail() != null) {
            response.setThumbnail(mediaService.convert(blogPost.getThumbnail()));
        }

        return response;
    }

    private BlogPostListResponse convertToListResponse(BlogPost blogPost) {
        BlogPostListResponse response = new BlogPostListResponse();
        response.setId(blogPost.getId());
        response.setTitle(blogPost.getTitle());
        response.setSlug(blogPost.getSlug());
        response.setShortDescription(blogPost.getShortDescription());
        response.setVisible(blogPost.isVisible());
        response.setPublishedAt(blogPost.getPublishedAt());
        response.setCreatedDate(blogPost.getCreatedDate() != null ? blogPost.getCreatedDate().toLocalDateTime() : null);

        if (blogPost.getThumbnail() != null) {
            response.setThumbnail(mediaService.convert(blogPost.getThumbnail()));
        }

        return response;
    }
}
