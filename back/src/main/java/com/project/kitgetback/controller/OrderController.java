package com.project.kitgetback.controller;

import com.project.kitgetback.DTO.Order;
import com.project.kitgetback.DTO.OrderRequest;
import com.project.kitgetback.entity.OrderEntity;
import com.project.kitgetback.repository.OrderRepository;
import com.project.kitgetback.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    // 등록
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        orderService.saveOrder(req, email);

        return ResponseEntity.ok().build();
    }

    // 조회
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrderSummaries(Authentication authentication) {
        String email = authentication.getName();
        List<Order> orders = orderService.getOrderSummariesByEmail(email);
        return ResponseEntity.ok(orders);
    }

    // 취소
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();
        orderService.deleteOrder(orderId, email);
        return ResponseEntity.noContent().build();
    }
}
