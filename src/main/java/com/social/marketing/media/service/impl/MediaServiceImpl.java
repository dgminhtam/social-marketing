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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        if (Objects.isNull(media)) {
            return null;
        }
        MediaResponse mediaResponse = new MediaResponse();
        mediaResponse.setId(media.getId());
        mediaResponse.setName(media.getName());
        mediaResponse.setAltText(media.getAltText());
        mediaResponse.setUrlOriginal(media.getUrlOriginal());
        mediaResponse.setSize(media.getFileSizeInByte());
        Map<String, String> variants = media.getVariants();
        if (variants != null) {
            mediaResponse.setUrlLarge(variants.get("large"));
            mediaResponse.setUrlMedium(variants.get("medium"));
            mediaResponse.setUrlThumbnail(variants.get("thumbnail"));
        }

        return mediaResponse;
    }

    @Transactional
    @Override
    public Media create(MultipartFile file) {
        validateMultipartFile(file);
        UploadResult result = fileService.uploadAndCreateVariants(file);
        Media media = new Media();
        media.setName(result.getName());
        media.setAltText(result.getName());
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

    @Override
    public Media get(Long id) {
        if (id == null) {
            return null;
        }
        Optional<Media> mediaOpt = mediaRepository.findById(id);
        return mediaOpt.orElse(null);
    }

    @Override
    public List<Media> getAllByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return mediaRepository.findAllById(ids);
    }

    @Override
    public Page<MediaResponse> getMedias(Specification<Media> specification, Pageable pageable) {
        Page<Media> medias = mediaRepository.findAll(specification, pageable);
        List<MediaResponse> clientProductResponse = medias.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(clientProductResponse, medias.getPageable(), medias.getTotalElements());
    }
}
