package com.social.marketing.repository;

import com.social.marketing.entity.SocialService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialServiceRepository extends JpaRepository<SocialService, Long> {
}
