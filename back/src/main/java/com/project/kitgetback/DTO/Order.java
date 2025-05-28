package com.project.kitgetback.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long orderId,                // 주문번호
        LocalDateTime orderDate,     // 주문일시
        List<String> itemNames,      // 주문상품명 리스트
        int totalPrice,              // 총결제금액
        String paymentMethod,        // 결제수단
        String shippingMethod        // 배송방법
) {
}
