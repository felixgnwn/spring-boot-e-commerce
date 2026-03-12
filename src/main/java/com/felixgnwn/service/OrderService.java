package com.felixgnwn.service;

import com.felixgnwn.dto.OrderItemResponse;
import com.felixgnwn.dto.OrderResponse;
import com.felixgnwn.entity.*;
import com.felixgnwn.repository.CartRepository;
import com.felixgnwn.repository.OrderRepository;
import com.felixgnwn.repository.CouponRepository;
import com.felixgnwn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public OrderResponse placeOrder(Long userId) {
        return placeOrder(userId, null);
    }

    @Transactional
    public OrderResponse placeOrder(Long userId, String couponCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place order with an empty cart");
        }

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .priceAtPurchase(cartItem.getProduct().getPrice())
                .build()
        ).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = BigDecimal.ZERO;
        String appliedCouponCode = null;

        if (couponCode != null && !couponCode.isBlank()) {
            var coupon = couponRepository.findByCodeIgnoreCaseAndActiveTrue(couponCode)
                    .orElseThrow(() -> new RuntimeException("Invalid or inactive coupon code"));

            if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Coupon has expired");
            }

            discountAmount = total
                    .multiply(coupon.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (discountAmount.compareTo(total) > 0) {
                discountAmount = total;
            }

            appliedCouponCode = coupon.getCode();
        }

        BigDecimal finalTotal = total.subtract(discountAmount);

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(finalTotal)
                .discountAmount(discountAmount)
                .couponCode(appliedCouponCode)
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        Order saved = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return toResponse(saved);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByUser(Long userId,
                                               OrderStatus status,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate) {
        return orderRepository.findByUserIdAndFilters(userId, status, startDate, endDate)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId) {
        return toResponse(orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId)));
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to cancel this order");
        }

        if (order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order cannot be cancelled in its current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus(status);
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(item -> {
            BigDecimal subtotal = item.getPriceAtPurchase()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            return OrderItemResponse.builder()
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .quantity(item.getQuantity())
                    .priceAtPurchase(item.getPriceAtPurchase())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .items(items)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .couponCode(order.getCouponCode())
                .discountAmount(order.getDiscountAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }
}