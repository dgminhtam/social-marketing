package com.social.marketing.cart.controller;

import com.social.marketing.cart.model.request.AddToCartRequest;
import com.social.marketing.cart.model.request.PlaceOrderRequest;
import com.social.marketing.cart.model.request.UpdateCartEmailRequest;
import com.social.marketing.cart.model.request.UpdateCartEntryRequest;
import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.cart.service.CartService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storefront/carts")
public class StorefrontCartController {

    @Resource
    private CartService cartService;

    /**
     * Lấy giỏ hàng theo link
     */
    @GetMapping
    public CartResponse getCart(@RequestParam("link") String link) {
        return cartService.getCart(link);
    }

    /**
     * Thêm một sản phẩm vào giỏ hàng
     */
    @PostMapping("/items")
    public CartResponse addToCart(@RequestParam("link") String link, @Valid @RequestBody AddToCartRequest request) {
        return cartService.addSingleItem(link, request);
    }

    /**
     * Thêm nhiều sản phẩm vào giỏ hàng (legacy method - tương thích với code cũ)
     */
    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody PlaceOrderRequest request) {
        return cartService.addToCart(request);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @PutMapping("/entries/{entryId}")
    public CartResponse updateCartEntry(
            @RequestParam("link") String link,
            @PathVariable Long entryId,
            @Valid @RequestBody UpdateCartEntryRequest request) {
        return cartService.updateCartEntry(link, entryId, request);
    }

    /**
     * Xóa một sản phẩm khỏi giỏ hàng
     */
    @DeleteMapping("/entries/{entryId}")
    public CartResponse removeCartEntry(@RequestParam("link") String link, @PathVariable Long entryId) {
        return cartService.removeCartEntry(link, entryId);
    }

    /**
     * Xóa tất cả sản phẩm trong giỏ hàng
     */
    @DeleteMapping
    public CartResponse clearCart(@RequestParam("link") String link) {
        return cartService.clearCart(link);
    }

    /**
     * Cập nhật email cho giỏ hàng
     */
    @PutMapping("/email")
    public CartResponse updateEmail(@RequestParam("link") String link,
            @Valid @RequestBody UpdateCartEmailRequest request) {
        return cartService.updateEmail(link, request);
    }

    /**
     * Checkout giỏ hàng
     */
    @PostMapping("/checkout")
    public CartResponse checkOut(@RequestParam("link") String link) {
        return cartService.checkout(link);
    }

    /**
     * Place order trực tiếp (thêm vào giỏ và checkout luôn)
     */
    @PostMapping("/place-order")
    public CartResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        cartService.addToCart(request);
        return cartService.checkout(request.link());
    }

}
