package com.social.marketing.order.service.impl;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.integration.payos.model.request.PayOSRequestPaymentRequest;
import com.social.marketing.integration.payos.model.response.PayOSRequestPaymentResponse;
import com.social.marketing.integration.payos.service.PayOSService;
import com.social.marketing.order.entity.Order;
import com.social.marketing.order.entity.OrderStatus;
import com.social.marketing.order.entity.PaymentTransaction;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.model.response.PaymentResponse;
import com.social.marketing.order.repository.OrderRepository;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.service.ClientProductService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private ClientProductService productService;

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private PayOSService payOSService;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        if (request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0.");
        }
        Product product = productService.getBySku(request.sku());
        if (Objects.isNull(product.getBase()) && CollectionUtils.isNotEmpty(product.getVariants())) {
            throw new BadRequestException("Can't place order because the product is base.");
        }
        if (Objects.isNull(product.getPrice())) {
            throw new BadRequestException("Product price cannot be null.");
        }
        Order order = buildOrder(request, product);
        orderRepository.save(order);
        return convert(order);
    }

    private Order buildOrder(PlaceOrderRequest request, Product product) {
        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(request.quantity());
        order.setEmail(request.email());
        order.setDescription(request.description());
        order.setOrderStatus(OrderStatus.OPEN);
        order.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
        return order;
    }

    @Override
    public OrderResponse convert(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setId(order.getId());
        orderResponse.setDescription(order.getDescription());
        orderResponse.setSubTotal(order.getSubTotal());
        return orderResponse;
    }

    @Override
    public Page<OrderResponse> getOrders(Specification<Order> specification, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(orderResponses, orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new NotFoundException("Order not found.");
        }
        return orderOpt.get();
    }

    @Override
    public PaymentResponse requestPayment(Long id) {
        Order order = getOrderById(id);
        PayOSRequestPaymentRequest paymentRequest = new PayOSRequestPaymentRequest();
        paymentRequest.setOrderCode(order.getId());
        paymentRequest.setAmount(order.getSubTotal().intValue());
        paymentRequest.setDescription(order.getId().toString());
        PayOSRequestPaymentResponse response = payOSService.requestPayment(paymentRequest);
        PaymentTransaction paymentTransaction = buildPaymentTransaction(response, order);
        order.getPaymentTransactions().add(paymentTransaction);
        orderRepository.save(order);
        return convertPaymentResponse(response);
    }

    @Override
    public Page<OrderResponse> getOrdersByEmail(String email, Specification<Order> specification, Pageable pageable) {
        Specification<Order> spec =
                (root, query, builder) -> builder.equal(root.get(Order.Fields.email), email);
        Page<Order> orders = orderRepository.findAll(spec.and(specification), pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(orderResponses, orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    private PaymentTransaction buildPaymentTransaction(PayOSRequestPaymentResponse response, Order order) {
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setBin(response.getData().getBin());
        paymentTransaction.setAccountNumber(response.getData().getAccountNumber());
        paymentTransaction.setAccountName(response.getData().getAccountName());
        paymentTransaction.setAmount(BigDecimal.valueOf(response.getData().getAmount()));
        paymentTransaction.setDescription(response.getData().getDescription());
        paymentTransaction.setCurrencyCode(response.getData().getCurrency());
        paymentTransaction.setStatus(response.getData().getStatus());
        paymentTransaction.setCheckoutUrl(response.getData().getCheckoutUrl());
        paymentTransaction.setQrCode(response.getData().getQrCode());
        paymentTransaction.setExternalId(response.getData().getPaymentLinkId());
        paymentTransaction.setOrder(order);
        return paymentTransaction;
    }

    private PaymentResponse convertPaymentResponse(PayOSRequestPaymentResponse payOSRequestPaymentResponse) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCheckoutUrl(payOSRequestPaymentResponse.getData().getCheckoutUrl());
        return paymentResponse;
    }
}
