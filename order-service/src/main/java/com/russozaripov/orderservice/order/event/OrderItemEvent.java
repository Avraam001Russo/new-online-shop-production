package com.russozaripov.orderservice.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEvent {
    private String sku_Code;
    private int quantity;
    private int price;
}
