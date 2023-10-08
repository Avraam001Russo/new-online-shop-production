package com.russozaripov.orderservice.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoDTO {
    private String email;
    private String deliveryAddress;
    private String phoneNumber;
    private List<OrderItemDTO> orderItemDTOCollection;
}
