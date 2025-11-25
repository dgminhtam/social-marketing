package com.social.marketing.media.service;

import com.social.marketing.media.entity.Media;
import com.social.marketing.media.model.response.MediaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    void delete(Media media);

    void validateMultipartFile(MultipartFile file);

    MediaResponse convert(Media media);

    Media create(MultipartFile file);

    MediaResponse upload(MultipartFile file);

    Media get(Long id);

    List<Media> getAllByIds(List<Long> ids);

    Page<MediaResponse> getMedias(Specification<Media> specification, Pageable pageable);

    MediaResponse getMedia(Long id);

    void deleteMedia(Long id);
}
