package com.social.marketing.cart.service;

import com.social.marketing.cart.entity.Cart;
import com.social.marketing.cart.model.request.OrderEntryRequest;
import com.social.marketing.cart.model.request.PlaceOrderRequest;
import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.cart.repository.CartRepository;
import com.social.marketing.exception.BadRequestException;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.service.StorefrontProductService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Resource
    private CartRepository cartRepository;

    @Resource
    private StorefrontProductService storefrontProductService;

    @Resource
    private OrderService orderService;

    public Cart getByLink(String link) {
        Optional<Cart> cart = cartRepository.findByDescription(link);
        return cart.orElseGet(() -> createEmptyCartForLink(link));
    }

    private Cart createEmptyCartForLink(String link) {
        Cart cart = new Cart();
        cart.setDescription(link);
        cart.setSubTotal(BigDecimal.ZERO);
        cart.setGrandTotal(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    @Transactional
    public CartResponse addToCart(PlaceOrderRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }
        Cart cart = getByLink(request.link());
        if (request.entries() == null || request.entries().isEmpty()) {
            throw new BadRequestException("No product selected.");
        }
        request.entries().forEach(entry -> addEntry(cart, entry));
        recalc(cart);
        cartRepository.save(cart);
        return convert(cart);
    }

    private void addEntry(Cart cart, OrderEntryRequest entry) {
        Product product = storefrontProductService.getBySku(entry.sku());
        if (product == null) {
            throw new BadRequestException("Product not found: " + entry.sku());
        }
        BigDecimal price = product.getPrice();
        if (price == null) {
            throw new BadRequestException("Product price cannot be null for sku: " + entry.sku());
        }
        // try to find existing entry with same product
        var existing = cart.getEntries().stream()
                .filter(e -> e.getProduct() != null && e.getProduct().getSku().equals(entry.sku()))
                .findFirst();
        if (existing.isPresent()) {
            var e = existing.get();
            e.setQuantity(e.getQuantity() + entry.quantity());
            e.setSubTotal(e.getPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
        } else {
            com.social.marketing.cart.entity.CartEntry cartEntry = new com.social.marketing.cart.entity.CartEntry();
            cartEntry.setCart(cart);
            cartEntry.setProduct(product);
            cartEntry.setPrice(price);
            cartEntry.setQuantity(entry.quantity());
            cartEntry.setName(product.getName());
            cartEntry.setDescription(entry.description());
            cartEntry.setSubTotal(price.multiply(BigDecimal.valueOf(entry.quantity())));
            cart.getEntries().add(cartEntry);
        }
    }

    private void recalc(Cart cart) {
        BigDecimal subTotal = cart.getEntries().stream()
                .map(e -> e.getSubTotal() == null ? BigDecimal.ZERO : e.getSubTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubTotal(subTotal);
        cart.setGrandTotal(subTotal); // no taxes/fees for now
    }

    @Transactional
    public CartResponse checkout(String link) {
        Cart cart = getByLink(link);
        if (cart.getEntries().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        // Build order request for OrderService
        List<com.social.marketing.order.model.request.OrderEntryRequest> entries = cart.getEntries().stream()
                .map(e -> new com.social.marketing.order.model.request.OrderEntryRequest(e.getProduct().getSku(), e.getQuantity(), e.getDescription()))
                .collect(Collectors.toList());
        // delegate to orderService to place order
        orderService.placeOrder(new com.social.marketing.order.model.request.PlaceOrderRequest(link, cart.getEmail() == null ? "" : cart.getEmail(), entries, cart.getDescription()));
        // mark cart as checked out by emptying entries
        cart.getEntries().clear();
        cart.setSubTotal(BigDecimal.ZERO);
        cart.setGrandTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
        return convert(cart);
    }

    public CartResponse getCart(String link) {
        return convert(getByLink(link));
    }

    public CartResponse convert(Cart cart) {
        CartResponse resp = new CartResponse();
        resp.setId(cart.getId());
        resp.setEmail(cart.getEmail());
        resp.setLink(cart.getDescription());
        resp.setSubTotal(cart.getSubTotal());
        resp.setCreateDate(cart.getCreatedDate());
        resp.setLastModifiedDate(cart.getLastModifiedDate());
        return resp;
    }
}
