package com.russozaripov.productservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO implements Serializable {
    private int productId;
    private boolean isInStock;
    private String skuCode;
    private String title;
    private String productType;
    private String productBrand;
    private int price;
    private String description;
    private LocalTime localTime;
}
