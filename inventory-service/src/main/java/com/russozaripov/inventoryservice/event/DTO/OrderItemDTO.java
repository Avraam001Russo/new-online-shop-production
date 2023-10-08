package com.russozaripov.inventoryservice.event.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String sku_Code;
    private int quantity;
    private int price;
}
