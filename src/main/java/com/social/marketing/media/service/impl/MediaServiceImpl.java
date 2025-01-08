package com.social.marketing.media.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.configuration.MediaProperties;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.model.response.MediaResponse;
import com.social.marketing.media.respository.MediaRepository;
import com.social.marketing.media.service.FileService;
import com.social.marketing.media.service.MediaService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class MediaServiceImpl implements MediaService {

    @Resource
    private MediaRepository mediaRepository;

    @Resource
    private FileService fileService;

    @Resource
    private MediaProperties properties;

    @Transactional
    @Override
    public Media create(MultipartFile file) {
        validateMultipartFile(file);
        Media media = new Media();
        media.setFileName(file.getOriginalFilename());
        media.setRealFileName(file.getOriginalFilename());
        media.setDescription(file.getOriginalFilename());
        media.setFileSizeInByte(file.getSize());
        media.setAltText(file.getOriginalFilename());
        media.setPath(properties.getUploadDir());
        media.setUrl(fileService.uploadFile(file, properties.getUploadDir()));
        media.setMimeType(fileService.detachMimeType(file));
        return mediaRepository.save(media);
    }

    @Transactional
    @Override
    public void delete(Media media) {
        fileService.deleteFile(media.getFileName(), media.getPath());
        mediaRepository.delete(media);
    }

    @Override
    public void validateMultipartFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File is empty!");
        }

        if (file.getSize() > properties.getMaxSize()) {
            throw new IllegalArgumentException("File exceeds maximum size of " + properties.getMaxSize() + " bytes.");
        }
        String mimeType = fileService.detachMimeType(file);
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
}
