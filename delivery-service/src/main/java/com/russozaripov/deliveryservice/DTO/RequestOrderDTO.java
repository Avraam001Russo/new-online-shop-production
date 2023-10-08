package com.russozaripov.deliveryservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDTO {
    private int orderID;
    private OrderInfoDTO orderInfoDTO;
    private String username;
}
