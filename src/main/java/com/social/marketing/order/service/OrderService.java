package com.social.marketing.order.service;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {

    OrderResponse placeOrder(PlaceOrderRequest request);

    OrderResponse convert(Order order);

    Page<OrderResponse> getOrders(Specification<Order> specification, Pageable pageable);
}
