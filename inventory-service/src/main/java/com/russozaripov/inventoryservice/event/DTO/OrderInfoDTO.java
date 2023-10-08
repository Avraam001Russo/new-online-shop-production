package com.russozaripov.inventoryservice.event.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDTO {
    private String email;
    private String deliveryAddress;
    private String phoneNumber;
    private Collection<OrderItemDTO> orderItemDTOCollection;
}
