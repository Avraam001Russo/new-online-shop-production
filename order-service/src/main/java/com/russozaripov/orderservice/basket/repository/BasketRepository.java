package com.russozaripov.orderservice.basket.repository;

import com.russozaripov.orderservice.basket.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {
        Optional<Basket> findBasketByUsername(String username);
}
