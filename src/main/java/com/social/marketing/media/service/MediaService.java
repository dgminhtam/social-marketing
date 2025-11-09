package com.social.marketing.media.service;

import com.social.marketing.media.entity.Media;
import com.social.marketing.media.model.response.MediaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    void delete(Media media);

    void validateMultipartFile(MultipartFile file);

    MediaResponse convert(Media media);

    Media create(MultipartFile file);

    MediaResponse upload(MultipartFile file);

    Media get(long id);
}
