package com.russozaripov.orderservice.basket.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketItemDTO {
    private String skuCode;
    private int price;
    private int quantity;
}
