package com.russozaripov.orderservice.repository;

import com.russozaripov.orderservice.basket.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Integer> {
    Optional<Basket> findBasketByUsername(String username);
}
