package com.social.marketing.order.controller;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderDetailResponse;
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
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @PostMapping("/{code}/request-payment")
    public PaymentResponse requestPayment(@PathVariable String code) {
        return orderService.requestPayment(code);
    }

    @GetMapping("/{code}")
    public OrderDetailResponse getOrderDetail(@PathVariable String code) {
        return orderService.getOrderDetail(code);
    }

    @GetMapping("/by-email/{email}")
    public Page<OrderResponse> getOrders(@PathVariable String email, @Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrdersByEmail(email, specification, pageable);
    }

    @GetMapping
    public Page<OrderResponse> getOrders(@Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrders(specification, pageable);
    }
}
