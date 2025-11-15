package com.social.marketing.media.controller;

import com.social.marketing.media.entity.Media;
import com.social.marketing.media.model.response.MediaResponse;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.model.response.ProductResponse;
import com.social.marketing.search.anotation.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public MediaResponse upload(@RequestParam("file") MultipartFile file) {
        return mediaService.upload(file);
    }

    @GetMapping
    public Page<MediaResponse> getMedias(@Search Specification<Media> specification, Pageable pageable) {
        return mediaService.getMedias(specification, pageable);
    }
}