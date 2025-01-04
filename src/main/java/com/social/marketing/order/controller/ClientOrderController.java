package com.social.marketing.order.controller;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.model.response.PaymentResponse;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/orders")
public class ClientOrderController {

    @Resource
    private OrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @PostMapping("/{id}/request-payment")
    public PaymentResponse requestPayment(@PathVariable Long id) {
        return orderService.requestPayment(id);
    }

    @GetMapping("/by-email/{email}")
    public Page<OrderResponse> getOrders(@PathVariable String email, @Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrdersByEmail(email, specification, pageable);
    }
}
