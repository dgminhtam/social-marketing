package com.social.marketing.user.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.repository.UserRepository;
import com.social.marketing.user.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MediaService mediaService;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void syncUser(String clerkUserId, String email, String firstName, String lastName, String imageUrl) {
        User user = getUserByEmail(email);
        if (user == null) {
            user = new User();
        }
        user.setExternalId(clerkUserId);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (StringUtils.isNotBlank(imageUrl)) {
            Media media = new Media();
            media.setUrlOriginal(imageUrl);
            user.setImage(media);
            mediaService.save(media);
        }
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        String externalId = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName).orElse(null);
        if (externalId == null) {
            return null;
        } else {
            return userRepository.findByExternalId(externalId).orElse(null);
        }
    }

    @Override
    public User getUserByExternalId(String externalId) {
        return userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new NotFoundException("User not found with externalId: " + externalId));
    }

}
