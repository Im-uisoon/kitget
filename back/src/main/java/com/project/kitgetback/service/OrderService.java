package com.project.kitgetback.service;

import com.project.kitgetback.DTO.Order;
import com.project.kitgetback.DTO.OrderItem;
import com.project.kitgetback.DTO.OrderRequest;
import com.project.kitgetback.entity.OrderEntity;
import com.project.kitgetback.entity.OrderItemEntity;
import com.project.kitgetback.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 주문정보 등록
    @Transactional
    public void saveOrder(OrderRequest req, String email) {
        OrderEntity order = new OrderEntity();
        order.setSchoolCode(req.schoolCode());
        order.setPaymentMethod(req.paymentMethod());
        order.setCardNumber(req.cardNumber());
        order.setShippingMethod(req.shippingMethod());
        order.setPostplace(req.postplace());
        order.setCreatedAt(LocalDateTime.now());
        order.setEmail(email); // 주문자 이메일 저장

        List<OrderItemEntity> itemEntities = new ArrayList<>();
        for (OrderItem itemDto : req.items()) {
            OrderItemEntity item = new OrderItemEntity();
            item.setName(itemDto.name());
            item.setPrice(itemDto.price());
            item.setOrder(order);
            itemEntities.add(item);
        }
        order.setItems(itemEntities);

        orderRepository.save(order);
    }

    // 주문정보 조회
    @Transactional(readOnly = true)
    public List<Order> getOrderSummariesByEmail(String email) {
        List<OrderEntity> orders = orderRepository.findByEmail(email);
        return orders.stream()
                .map(order -> new Order(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getItems().stream()
                                .map(OrderItemEntity::getName)
                                .collect(Collectors.toList()),
                        order.getItems().stream()
                                .mapToInt(OrderItemEntity::getPrice)
                                .sum(),
                        order.getPaymentMethod(),
                        order.getShippingMethod()
                ))
                .collect(Collectors.toList());
    }

    // 취소
    @Transactional
    public void deleteOrder(Long orderId, String email) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        if (!order.getEmail().equals(email)) {
            throw new RuntimeException("본인 주문만 취소할 수 있습니다.");
        }
        orderRepository.delete(order);
    }
}
