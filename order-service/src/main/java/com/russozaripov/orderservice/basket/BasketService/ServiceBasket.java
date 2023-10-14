package com.russozaripov.orderservice.basket.BasketService;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.BasketItemDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.exceptionHandler.BasketException;
import com.russozaripov.orderservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceBasket {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBasket.class);
    private final ServiceJWT serviceJWT;
    private final BasketRepository basketRepository;
    public Basket createBasket(BasketDTO basketDTO, String authorization){
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        Basket basket = Basket.builder()
                .username(username)
                .build();
        for (BasketItemDTO itemDTO : basketDTO.getBasketItemDTO()){
            ProductInBasket productInBasket = ProductInBasket.builder()
                    .skuCode(itemDTO.getSkuCode())
                    .price(itemDTO.getPrice())
                    .quantity(itemDTO.getQuantity())
                    .build();
            basket.addProductToBasket(productInBasket);
        }
             LOGGER.info("Basket saved successfully: %s".formatted(basket.getUsername()));
             return basketRepository.save(basket);

    }

    @CacheEvict(cacheNames = "basket", key = "#username")
    public void deleteBasket(String username) {
        Optional<Basket> basketByUsername = basketRepository.findBasketByUsername(username);
        if (basketByUsername.isPresent()){
            basketRepository.deleteById(basketByUsername.get().getId());
            LOGGER.info("Basket deleted successfully!");
        }
        else {
            throw new BasketException("Basket not found.");
        }
    }

    @CachePut(cacheNames = "basket", key = "#username")
    public Basket updateBasket(BasketDTO basketDTO, String username) {
        Optional<Basket> basketByUsername = basketRepository.findBasketByUsername(username);
        if (basketByUsername.isPresent()){
            Basket basket = basketByUsername.get();
            for (BasketItemDTO itemDTO : basketDTO.getBasketItemDTO()){
                ProductInBasket productInBasket = ProductInBasket.builder()
                        .skuCode(itemDTO.getSkuCode())
                        .price(itemDTO.getPrice())
                        .quantity(itemDTO.getQuantity())
                        .build();
                boolean found = false;
                for (ProductInBasket prod : basket.getProductsInBaskets()){
                    if (itemDTO.getSkuCode().equals(prod.getSkuCode())){
                        int quantity = prod.getQuantity() + itemDTO.getQuantity();
                        prod.setQuantity(quantity);
                        found = true;
                    }
                }
                if (!found){
                basket.addProductToBasket(productInBasket);
                }
            }
            LOGGER.info("Basket updated successfully! -> %s".formatted(basket.getUsername()));
            return basketRepository.save(basket);
        }
        else {
            throw new BasketException("Basket not found.");
        }
    }
    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }
    @Cacheable(cacheNames = "basket", key = "#username")
    public Basket getSingleBasket(String username){
        Optional<Basket> basketByUsername = basketRepository.findBasketByUsername(username);
        if (basketByUsername.isPresent()){
            Basket basket = basketByUsername.get();
            LOGGER.info("%s -> get basket by username methode has come".formatted(basket.getUsername()));
            return basket;
        }
        else {
            throw new BasketException("Basket not found.");
        }
    }

}
