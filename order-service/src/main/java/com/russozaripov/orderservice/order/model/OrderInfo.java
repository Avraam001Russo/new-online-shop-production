package com.russozaripov.orderservice.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "order_info")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderInfo")
    private Collection<OrderItem> orderItemCollection;

    public void addOrderItemsInOrderInfo(OrderItem orderItem){
        if (orderItemCollection == null){
            orderItemCollection = new ArrayList<>();
        }
        orderItemCollection.add(orderItem);
        orderItem.setOrderInfo(this);
    }


}
