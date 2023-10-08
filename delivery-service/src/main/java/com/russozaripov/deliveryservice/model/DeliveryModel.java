package com.russozaripov.deliveryservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders_to_delivery")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "username")
    private String username;
    @Column(name = "delivery_price")
    private int deliveryPrice;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @OneToMany(mappedBy = "ordersToDelivery", cascade = CascadeType.ALL)
    private List<OrderItem> listOfOrderItem;
}
