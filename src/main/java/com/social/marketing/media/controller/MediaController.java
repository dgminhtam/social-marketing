package com.social.marketing.media.controller;

import com.social.marketing.media.model.response.MediaResponse;
import com.social.marketing.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}