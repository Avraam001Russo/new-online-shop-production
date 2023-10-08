package com.russozaripov.productservice.DTO.product_to_productDTO;

import com.russozaripov.productservice.DTO.ProductDTO;
import com.russozaripov.productservice.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class FromProductToProductDTO {
    public ProductDTO productToProductDTO(Product product){
       return ProductDTO.builder()
                .productId(product.getId())
               .title(product.getTitle())
                .isInStock(product.isInStock())
                .skuCode(product.getSkuCode())
                .productBrand(product.getBrand().getName())
                .productType(product.getType().getName())
                .price(product.getDetails().getPrice())
                .description(product.getDetails().getDescription())
                .localTime(LocalTime.now())
                .build();
    }
}
