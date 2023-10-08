package com.russozaripov.orderservice.order.DTO;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoAboutOrderDTO {
    @NotNull
    private String email;
    @NotNull
    private String deliveryAddress;
    @NotNull
    private String phoneNumber;
}
