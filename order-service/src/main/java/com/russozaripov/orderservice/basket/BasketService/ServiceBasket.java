package com.russozaripov.orderservice.basket.BasketService;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.BasketItemDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.basket.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceBasket {

    private final BasketRepository basketRepository;
    private final ServiceJWT serviceJWT;
    public RequestResponseDTO<String> createBasket(BasketDTO basketDTO, String authorization){
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        Basket basket;
            Optional<Basket> basketOptional = basketRepository.findBasketByUsername(username);

            if (basketOptional.isPresent()){
            basket = basketOptional.get();
        }
        else {
         basket = Basket.builder().username(username).build();
        }
         for (BasketItemDTO basketItemDTO : basketDTO.getBasketItemDTO()){
            ProductInBasket productInBasket = ProductInBasket.builder()
                     .price(basketItemDTO.getPrice())
                     .quantity(basketItemDTO.getQuantity())
                     .skuCode(basketItemDTO.getSkuCode())
                     .build();
              basket.addProductToBasket(productInBasket);
         }
             basketRepository.save(basket);
            log.info("Basket %s save".formatted(username));
            return new RequestResponseDTO<>("Success", "Basket %s save".formatted(username));
    }

    public RequestResponseDTO<String> deleteProduct(String skuCode, String authorization) {
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        Optional<Basket> basketOptional = basketRepository.findBasketByUsername(username);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            List<ProductInBasket> productInBasketList = (List<ProductInBasket>) basket.getProductsInBaskets();
            for (ProductInBasket prod : productInBasketList) {
                if (Objects.equals(skuCode, prod.getSkuCode())) {
                    int quantity = prod.getQuantity() - 1;
                    prod.setQuantity(quantity);
                }
            }
            basketRepository.save(basket);
            String success = "Product: %s successfully delete from basket.".formatted(skuCode);
            log.info(success);
            return new RequestResponseDTO<>(success, "Success.");
        } else {
            return new RequestResponseDTO<>("BASKET_NOT_FOUND_EXCEPTION", "Basket with username: %s not found.");
        }
    }

    public RequestResponseDTO<String> updateBasket(BasketDTO basketDTO, String authorization) {
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        Optional<Basket> basketOptional = basketRepository.findBasketByUsername(username);
        if (basketOptional.isPresent()){
        Basket basket = basketOptional.get();
        List<ProductInBasket> copyOfBasket = new ArrayList<>();
        for (BasketItemDTO basketItemDTO : basketDTO.getBasketItemDTO()){
           ProductInBasket productInBasket = ProductInBasket.builder()
                    .price(basketItemDTO.getPrice())
                    .quantity(basketItemDTO.getQuantity())
                    .skuCode(basketItemDTO.getSkuCode())
                    .build();

            boolean foundNewProduct = false;
           for (ProductInBasket prod : basket.getProductsInBaskets()){
               if (prod.getSkuCode().equals(productInBasket.getSkuCode())){
                   int QUANTITY = prod.getQuantity() + productInBasket.getQuantity();
                   prod.setQuantity(QUANTITY);
                   log.info("Product: %s update %d".formatted(prod.getSkuCode(), QUANTITY));
                   foundNewProduct = true;
                   break;
               }
           }
           if(!foundNewProduct){
               log.info("Product: %s added in basket".formatted(basketItemDTO.getSkuCode()));
               copyOfBasket.add(productInBasket);
           }
        }
        for (ProductInBasket product : copyOfBasket){
            basket.addProductToBasket(product);
        }
        basketRepository.save(basket);
        return new RequestResponseDTO<>("Basket: %s successfully update.".formatted(username), "Success.");
        } else {
            return new RequestResponseDTO<>("BASKET_NOT_FOUND_EXCEPTION", "Basket with username: %s not found");
        }
    }

}
