package com.russozaripov.orderservice.basket;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.BasketItemDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.repository.BasketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceBasketTests {
    @Mock
    private ServiceJWT serviceJWT;
    @Mock
    private BasketRepository basketRepository;
    @InjectMocks
    private ServiceBasket serviceBasket;
    BasketDTO basketDTO;
    Basket basket;
    @BeforeEach
    public void setUp(){
        basketDTO = BasketDTO.builder()
                .basketItemDTO(
                        List.of(BasketItemDTO.builder().quantity(1).skuCode("Apple").price(94990).build(),
                                BasketItemDTO.builder().quantity(2).skuCode("Samsung").price(107990).build())
                )
                .build();
        basket = new Basket();
        ProductInBasket productInBasket = new ProductInBasket(1, "Apple", 34889, 1, basket);
        ProductInBasket productInBasket_2 = new ProductInBasket(2, "Samsung", 26990, 2, basket);
        basket.setUsername("username");
        basket.setId(1);
        basket.addProductToBasket(productInBasket);
        basket.addProductToBasket(productInBasket_2);
    }
    @DisplayName("Junit test for create basket operation")
    @Test
    public void givenBasketDTO_whenCreateBasket_thenReturnBasketObject(){
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn("test-username");
        BDDMockito.given(basketRepository.save(ArgumentMatchers.any(Basket.class)))
                .willAnswer((invocation) -> {
                    Basket savedBasket = invocation.getArgument(0);
                    return Basket.builder()
                            .username(savedBasket.getUsername())
                            .productsInBaskets(savedBasket.getProductsInBaskets())
                            .id(1)
                            .build();
                });

        Basket basket = serviceBasket.createBasket(basketDTO, "Authorization");
        Assertions.assertThat(basket).isNotNull();
        Assertions.assertThat(basket.getUsername()).isEqualTo("test-username");
        Assertions.assertThat(basket.getId()).isEqualTo(1);
    }
    @DisplayName("Junit test for delete basket operation")
    @Test
    public void givenUsername_whenDeleteBasket_thenReturnNothing(){
        Basket basket = new Basket(1, "Russo", List.of(new ProductInBasket()));
        BDDMockito.given(basketRepository.findBasketByUsername(ArgumentMatchers.anyString()))
                .willReturn(Optional.of(basket));
        BDDMockito.willDoNothing().given(basketRepository).deleteById(ArgumentMatchers.anyInt());
        serviceBasket.deleteBasket("username");
        Mockito.verify(basketRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
    }
    @DisplayName("Junit test for update basket operation")
    @Test
    public void givenUsernameAndBasketDTO_whenUpdateBasket_thenReturnNothing(){

        BDDMockito.given(basketRepository.findBasketByUsername(ArgumentMatchers.anyString())).willReturn(Optional.of(basket));
        BDDMockito.given(basketRepository.save(ArgumentMatchers.any(Basket.class))).willAnswer((invocation) -> {
           return invocation.getArgument(0);
        });
        Basket responseBasket = serviceBasket.updateBasket(basketDTO, "username");
        Assertions.assertThat(responseBasket).isNotNull();
        Assertions.assertThat(responseBasket.getUsername()).isEqualTo("username");
        Assertions.assertThat(responseBasket.getId()).isEqualTo(1);

    }
    @DisplayName("junit test for get single basket operation.")
    @Test
    public void givenBasketObject_whenGetSingleBasket_thenReturnBasketObject(){
        BDDMockito.given(basketRepository.findBasketByUsername(ArgumentMatchers.anyString())).willReturn(Optional.of(basket));
        Basket savedBasket = serviceBasket.getSingleBasket("username");
        Assertions.assertThat(savedBasket).isNotNull();
        Assertions.assertThat(savedBasket.getId()).isEqualTo(1);
        Assertions.assertThat(savedBasket.getUsername()).isEqualTo("username");


    }
    @DisplayName("junit test for get all baskets operation.")
    @Test
    public void givenBasketList_whenGetAllBaskets_thenReturnBasketList(){
        List<Basket> basketList = new ArrayList<>(List.of(basket, Basket.builder().build()));
        BDDMockito.given(basketRepository.findAll()).willReturn(basketList);
        List<Basket> response = serviceBasket.getAllBaskets();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(2);
    }

}
