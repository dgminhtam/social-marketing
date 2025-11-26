package com.social.marketing.cart.controller;

import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.cart.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * Lấy danh sách tất cả giỏ hàng (Admin)
     */
    @GetMapping
    public Page<CartResponse> getAllCarts(Pageable pageable) {
        return cartService.getAllCarts(pageable);
    }

    /**
     * Lấy chi tiết giỏ hàng theo ID (Admin)
     */
    @GetMapping("/{id}")
    public CartResponse getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    /**
     * Xóa giỏ hàng theo ID (Admin)
     */
    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
    }
}
