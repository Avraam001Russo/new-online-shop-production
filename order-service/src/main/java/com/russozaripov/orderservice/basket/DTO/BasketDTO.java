package com.russozaripov.orderservice.basket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {
    private Collection<BasketItemDTO> basketItemDTO;
}
