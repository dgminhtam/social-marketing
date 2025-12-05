package com.social.marketing.cart.repository;

import com.social.marketing.cart.entity.Cart;
import com.social.marketing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findBySid(String description);

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByEmail(String email);

    void deleteBySid(String description);
}
