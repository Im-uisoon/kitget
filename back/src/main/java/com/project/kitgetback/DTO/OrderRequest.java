package com.project.kitgetback.DTO;

import java.util.List;

public record OrderRequest(
        String schoolCode,
        String paymentMethod,
        String cardNumber,
        String shippingMethod,
        String postplace,
        List<OrderItem> items
) {}
