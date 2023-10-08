package com.russozaripov.orderservice.basket.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "basket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "basket")
    @JsonManagedReference
    private Collection<ProductInBasket> productsInBaskets;

    public void addProductToBasket(ProductInBasket productInBasket){
        if (this.productsInBaskets == null){
            productsInBaskets = new ArrayList<>();
        }
        this.productsInBaskets.add(productInBasket);
        productInBasket.setBasket(this);
    }
}
