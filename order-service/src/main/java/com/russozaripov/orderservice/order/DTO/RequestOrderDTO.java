package com.russozaripov.orderservice.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestOrderDTO {
    private int orderID;
    private OrderInfoDTO orderInfoDTO;
    private String username;
}
