package com.nagp.ms.orderService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    public enum OrderStatus {
        CONFIRMED,
        CANCELLED
    }
    public enum PaymentStatus {
        PAID,
        NOT_PAID,
        FAILED
    }
    private String id;
    private String username;
    private List<ProductEntry> productEntry;
    private double totalAmount;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    public Order(String id, String name, List<ProductEntry> cart,  double price,  OrderStatus orderStatus , PaymentStatus paymentStatus) {
        this.id = id;
        this.username = name;
        this.productEntry = cart;
        this.totalAmount = price;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
    }
}
