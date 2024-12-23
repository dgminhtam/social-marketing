package com.social.marketing.order.service.impl;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.repository.OrderRepository;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private ProductService productService;

    @Resource
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        if (request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0.");
        }
        Product product = productService.getBySku(request.sku());
        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(request.quantity());
        order.setEmail(request.email());
        order.setDescription(request.description());
        BigDecimal price = product.getPrice();
        if (Objects.isNull(price)) {
            throw new BadRequestException("Product price cannot be null.");
        }
        order.setSubTotal(price.multiply(BigDecimal.valueOf(request.quantity())));
        orderRepository.save(order);
        return convert(order);
    }

    @Override
    public OrderResponse convert(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setCode(order.getCode());
        orderResponse.setDescription(order.getDescription());
        orderResponse.setProduct(productService.convert(order.getProduct()));
        orderResponse.setSubTotal(order.getSubTotal());
        return orderResponse;
    }

    @Override
    public Page<OrderResponse> getOrders(Specification<Order> specification, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(orderResponses, orders.getPageable(), orders.getTotalElements());
    }
}
