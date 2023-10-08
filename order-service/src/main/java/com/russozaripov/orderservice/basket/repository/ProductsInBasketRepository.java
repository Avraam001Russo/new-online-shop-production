package com.russozaripov.orderservice.basket.repository;

import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductsInBasketRepository extends JpaRepository<ProductInBasket, Integer> {
    @Query("select prod from ProductInBasket prod where prod.basket = :basket and prod.skuCode = :skuCode")
    Optional<ProductInBasket> findProductInBasketByBasketAndSkuCode(@Param("basket") Basket basket, @Param("skuCode") String skuCode);
}
