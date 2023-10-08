package com.russozaripov.orderservice.basket.service;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.BasketItemDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.basket.repository.BasketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTests {
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private ServiceJWT serviceJWT;
    @InjectMocks
    private ServiceBasket serviceBasket;

    String username;
    String bearerToken;
    BasketDTO basketDTO;
    BasketItemDTO basketItemDTO_1;
    BasketItemDTO basketItemDTO_2;
    Basket basket;
    ProductInBasket productInBasket;
    String SKU_CODE;
    @BeforeEach
    public void setUp(){
        username = "RUSSO";
        bearerToken = "Bearer-token";
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn(username);

        basketDTO = new BasketDTO();
        basketItemDTO_1 = BasketItemDTO.builder().price(145990).quantity(1).skuCode("Apple Iphone 15 PRO").build();
        basketItemDTO_2 = BasketItemDTO.builder().price(101990).quantity(2).skuCode("Apple Iphone 14 PRO MAX").build();
        basketDTO.setBasketItemDTO(List.of(basketItemDTO_1, basketItemDTO_2));

        basket = new Basket();
        SKU_CODE = "Apple Iphone 15 PRO";
        productInBasket = ProductInBasket.builder()
                .id(1)
                .basket(basket)
                .skuCode(SKU_CODE)
                .quantity(3)
                .price(139990)
                .build();
        basket.setUsername(username);
        List<ProductInBasket> productInBasketList = new ArrayList<>();
        productInBasketList.add(productInBasket);
        basket.setProductsInBaskets(productInBasketList);
    }

    @DisplayName("Junit test for create basket operation.")
    @Test
    public void givenBasketDTO_whenCreateBasket_thenReturnRequestResponseDTO(){
         //given

         BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn(username);
         BDDMockito.given(basketRepository.findBasketByUsername(username)).willReturn(Optional.empty());
         BDDMockito.given(basketRepository.save(ArgumentMatchers.any(Basket.class))).willReturn(ArgumentMatchers.any(Basket.class));
         //when
         RequestResponseDTO<String> requestResponseDTO = serviceBasket.createBasket(basketDTO, bearerToken);
         // then
        Assertions.assertThat(requestResponseDTO).isNotNull();
        Assertions.assertThat(requestResponseDTO.getData()).isEqualTo("Success");
        Assertions.assertThat(requestResponseDTO.getMessage()).isEqualTo("Basket %s save".formatted(username));

    }
    @DisplayName("Junit test for delete product from basket operation.")
    @Test
    public void givenSkuCode_whenDeleteProduct_thenReturnRequestResponseDTO(){
        //given
        String SKU_CODE = "Apple Iphone 13 PRO";
        BDDMockito.given(basketRepository.findBasketByUsername(username)).willReturn(Optional.of(basket));
        BDDMockito.given(basketRepository.save(ArgumentMatchers.any(Basket.class))).willReturn(ArgumentMatchers.any(Basket.class));
        //when
        RequestResponseDTO<String> requestResponseDTO = serviceBasket.deleteProduct(SKU_CODE, bearerToken);
        //then
        Assertions.assertThat(requestResponseDTO).isNotNull();
        Assertions.assertThat(requestResponseDTO.getData()).isEqualTo("Product: %s successfully delete from basket.".formatted(SKU_CODE));
        Assertions.assertThat(requestResponseDTO.getMessage()).isEqualTo("Success.");
    }
    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenBasketDTO_whenUpdateBasket_thenReturnRequestResponseDTO(){
        //given
        BDDMockito.given(basketRepository.findBasketByUsername(username)).willReturn(Optional.of(basket));
        BDDMockito.given(basketRepository.save(ArgumentMatchers.any(Basket.class))).willReturn(ArgumentMatchers.any(Basket.class));
        //when
        RequestResponseDTO<String> requestResponseDTO = serviceBasket.updateBasket(basketDTO, bearerToken);
        //then
        Assertions.assertThat(requestResponseDTO).isNotNull();
        Assertions.assertThat(requestResponseDTO.getMessage()).isEqualTo("Success.");
        Assertions.assertThat(requestResponseDTO.getData()).isEqualTo("Basket: %s successfully update.".formatted(username));

    }
}
