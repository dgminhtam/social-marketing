package com.social.marketing.pcm.repository;

import com.social.marketing.pcm.entity.ProductCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCollectionRepository
        extends JpaRepository<ProductCollection, Long>, JpaSpecificationExecutor<ProductCollection> {
    Optional<ProductCollection> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
