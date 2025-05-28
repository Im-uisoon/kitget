package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_code")
    private String schoolCode;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "postplace")
    private String postplace;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items = new ArrayList<>();
}
