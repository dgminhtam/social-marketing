package com.social.marketing.media.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.model.request.UploadResult;
import com.social.marketing.media.model.response.MediaResponse;
import com.social.marketing.media.respository.MediaRepository;
import com.social.marketing.media.service.FileService;
import com.social.marketing.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final FileService fileService;
    private final MediaProperties properties;

    @Transactional
    @Override
    public void delete(Media media) {
        //TODO
    }

    @Override
    public void validateMultipartFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File is empty!");
        }

        if (file.getSize() > properties.getMaxSize()) {
            throw new IllegalArgumentException("File exceeds maximum size of " + properties.getMaxSize() + " bytes.");
        }
        String mimeType;
        try {
            mimeType = fileService.detachMimeType(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!properties.getAcceptMimeTypes().contains(mimeType)) {
            throw new IllegalArgumentException("File type not allowed: " + mimeType);
        }
    }

    @Override
    public MediaResponse convert(Media media) {
        MediaResponse mediaResponse = new MediaResponse();
        if (Objects.nonNull(media)) {
            BeanUtils.copyProperties(media, mediaResponse);
        }
        return mediaResponse;
    }

    @Transactional
    @Override
    public Media create(MultipartFile file) {
        validateMultipartFile(file);
        UploadResult result = fileService.uploadAndCreateVariants(file);
        Media media = new Media();
        media.setAltText(result.getAltText());
        media.setFileSizeInByte(result.getFileSizeInByte());
        media.setMimeType(result.getMimeType());
        media.setUrlOriginal(result.getUrlOriginal());
        media.setVariants(result.getVariants());
        return mediaRepository.save(media);
    }

    @Override
    public MediaResponse upload(MultipartFile file) {
        return convert(create(file));
    }
}
