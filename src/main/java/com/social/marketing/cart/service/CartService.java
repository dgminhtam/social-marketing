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
import com.social.marketing.user.entity.User;
import com.social.marketing.user.service.UserService;
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

    @Resource
    private UserService userService;

    public Cart getByLink(String link) {
        User user = userService.getCurrentUser();
        return getCartForUserOrLink(user, link);
    }

    private Cart getCartForUserOrLink(User user, String link) {
        if (user != null) {
            // 1. Try to find cart by User
            Optional<Cart> userCart = cartRepository.findByUser(user);
            if (userCart.isPresent()) {
                return userCart.get();
            }

            // 2. If User has no cart, check if there is an anonymous cart with the link
            Optional<Cart> linkCart = cartRepository.findByDescription(link);
            if (linkCart.isPresent()) {
                // Assign this anonymous cart to the user
                Cart cart = linkCart.get();
                if (cart.getUser() == null) {
                    cart.setUser(user);
                    return cartRepository.save(cart);
                }
            }

            // 3. Create new cart for User
            return createEmptyCartForUser(user, link);
        }

        // Anonymous flow
        Optional<Cart> cart = cartRepository.findByDescription(link);
        return cart.orElseGet(() -> createEmptyCartForLink(link));
    }

    private Cart createEmptyCartForUser(User user, String link) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setDescription(link); // Keep link as metadata or fallback
        cart.setEmail(user.getEmail());
        cart.setSubTotal(BigDecimal.ZERO);
        cart.setGrandTotal(BigDecimal.ZERO);
        return cartRepository.save(cart);
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
                .map(e -> new com.social.marketing.order.model.request.OrderEntryRequest(e.getProduct().getSku(),
                        e.getQuantity(), e.getDescription()))
                .collect(Collectors.toList());
        // delegate to orderService to place order
        orderService.placeOrder(new com.social.marketing.order.model.request.PlaceOrderRequest(link,
                cart.getEmail() == null ? "" : cart.getEmail(), entries, cart.getDescription()));
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
        resp.setGrandTotal(cart.getGrandTotal());
        resp.setCreateDate(cart.getCreatedDate());
        resp.setLastModifiedDate(cart.getLastModifiedDate());
        resp.setTotalItems(cart.getEntries().size());
        resp.setEntries(cart.getEntries().stream()
                .map(this::convertEntryToResponse)
                .collect(Collectors.toList()));
        return resp;
    }

    private com.social.marketing.cart.model.response.CartEntryResponse convertEntryToResponse(
            com.social.marketing.cart.entity.CartEntry entry) {
        com.social.marketing.cart.model.response.CartEntryResponse resp = new com.social.marketing.cart.model.response.CartEntryResponse();
        resp.setId(entry.getId());
        resp.setSku(entry.getProduct() != null ? entry.getProduct().getSku() : null);
        resp.setName(entry.getName());
        resp.setDescription(entry.getDescription());
        resp.setPrice(entry.getPrice());
        resp.setQuantity(entry.getQuantity());
        resp.setSubTotal(entry.getSubTotal());
        if (entry.getProduct() != null && entry.getProduct().getImage() != null) {
            resp.setImageUrl(entry.getProduct().getImage().getUrlOriginal());
        }
        return resp;
    }

    // ============ CRUD Operations ============

    public org.springframework.data.domain.Page<CartResponse> getAllCarts(
            org.springframework.data.domain.Pageable pageable) {
        return cartRepository.findAll(pageable).map(this::convert);
    }

    public CartResponse getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new com.social.marketing.exception.NotFoundException(
                        "Không tìm thấy giỏ hàng với ID: " + id));
        return convert(cart);
    }

    @Transactional
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new com.social.marketing.exception.NotFoundException("Không tìm thấy giỏ hàng với ID: " + id);
        }
        cartRepository.deleteById(id);
    }

    // ============ Cart Management Operations ============

    @Transactional
    public CartResponse addSingleItem(String link, com.social.marketing.cart.model.request.AddToCartRequest request) {
        if (request == null) {
            throw new BadRequestException("Request không được null");
        }
        Cart cart = getByLink(link);

        Product product = storefrontProductService.getBySku(request.sku());
        if (product == null) {
            throw new BadRequestException("Không tìm thấy sản phẩm: " + request.sku());
        }

        BigDecimal price = product.getPrice();
        if (price == null) {
            throw new BadRequestException("Giá sản phẩm không hợp lệ cho SKU: " + request.sku());
        }

        // Tìm entry có cùng product
        var existing = cart.getEntries().stream()
                .filter(e -> e.getProduct() != null && e.getProduct().getSku().equals(request.sku()))
                .findFirst();

        if (existing.isPresent()) {
            // Cập nhật số lượng nếu đã có
            var e = existing.get();
            e.setQuantity(e.getQuantity() + request.quantity());
            e.setSubTotal(e.getPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
        } else {
            // Thêm entry mới
            com.social.marketing.cart.entity.CartEntry cartEntry = new com.social.marketing.cart.entity.CartEntry();
            cartEntry.setCart(cart);
            cartEntry.setProduct(product);
            cartEntry.setPrice(price);
            cartEntry.setQuantity(request.quantity());
            cartEntry.setName(product.getName());
            cartEntry.setDescription(request.description());
            cartEntry.setSubTotal(price.multiply(BigDecimal.valueOf(request.quantity())));
            cart.getEntries().add(cartEntry);
        }

        recalc(cart);
        cartRepository.save(cart);
        return convert(cart);
    }

    @Transactional
    public CartResponse updateCartEntry(String link, Long entryId,
            com.social.marketing.cart.model.request.UpdateCartEntryRequest request) {
        Cart cart = getByLink(link);

        com.social.marketing.cart.entity.CartEntry entry = cart.getEntries().stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElseThrow(() -> new com.social.marketing.exception.NotFoundException(
                        "Không tìm thấy sản phẩm trong giỏ hàng"));

        entry.setQuantity(request.quantity());
        entry.setSubTotal(entry.getPrice().multiply(BigDecimal.valueOf(request.quantity())));

        recalc(cart);
        cartRepository.save(cart);
        return convert(cart);
    }

    @Transactional
    public CartResponse removeCartEntry(String link, Long entryId) {
        Cart cart = getByLink(link);

        boolean removed = cart.getEntries().removeIf(e -> e.getId().equals(entryId));
        if (!removed) {
            throw new com.social.marketing.exception.NotFoundException("Không tìm thấy sản phẩm trong giỏ hàng");
        }

        recalc(cart);
        cartRepository.save(cart);
        return convert(cart);
    }

    @Transactional
    public CartResponse clearCart(String link) {
        Cart cart = getByLink(link);
        cart.getEntries().clear();
        cart.setSubTotal(BigDecimal.ZERO);
        cart.setGrandTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
        return convert(cart);
    }

    @Transactional
    public CartResponse updateEmail(String link,
            com.social.marketing.cart.model.request.UpdateCartEmailRequest request) {
        Cart cart = getByLink(link);
        cart.setEmail(request.email());
        cartRepository.save(cart);
        return convert(cart);
    }
}
