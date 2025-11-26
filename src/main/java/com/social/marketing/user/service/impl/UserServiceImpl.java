package com.social.marketing.user.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.repository.UserRepository;
import com.social.marketing.user.service.UserService;
import lombok.AllArgsConstructor;

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
//        if (StringUtils.isNotBlank(imageUrl)) {
//            Media media = new Media();
//            media.setUrlOriginal(imageUrl);
//            user.setImage(media);
//            mediaService.save(media);
//        }
        userRepository.save(user);
    }
}
