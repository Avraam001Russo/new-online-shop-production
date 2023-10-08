package com.russozaripov.orderservice.order.service;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.basket.repository.BasketRepository;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import com.russozaripov.orderservice.order.orderService.messageBroker.BrokerMessage;
import com.russozaripov.orderservice.order.model.Order;
import com.russozaripov.orderservice.order.model.OrderInfo;
import com.russozaripov.orderservice.order.model.OrderItem;
import com.russozaripov.orderservice.order.orderRepository.OrderRepository;
import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private BrokerMessage brokerMessage;
    @Mock
    private ServiceJWT serviceJWT;
    @InjectMocks
    private ServiceOrder serviceOrder;

    @DisplayName("Junit test for create order operation.")
    @Test
    public void givenInfoAboutOrderDTO_whenCreateOrder_thenReturnRequestResponseDTO(){
        //given
        String username = "Russo";
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn(username);


        Basket basket = new Basket();
        ProductInBasket productInBasket_1 = new ProductInBasket(1, "Apple Iphone 13 pro", 54990, 1, basket);
        ProductInBasket productInBasket_2 = new ProductInBasket(2, "Apple Iphone 11", 35990, 2, basket);
        basket.setId(1);
        basket.setUsername(username);
        basket.setProductsInBaskets(List.of(productInBasket_1, productInBasket_2));

        InfoAboutOrderDTO infoAboutOrderDTO = new InfoAboutOrderDTO("russo@mail.ru", "Moscow, Tverskaya street, 7", "89779993473");
        BDDMockito.given(basketRepository.findBasketByUsername(username)).willReturn(Optional.of(basket));
        BDDMockito.given(orderRepository.save(ArgumentMatchers.any(Order.class)))
                .willAnswer((invocation) -> {
                    Order order = invocation.getArgument(0);
                    order.setId(1);
                    order.setOrderInfo( new OrderInfo(1, "russo@mail.ru", "Moscow, Tverskaya street, 7", "89779993473", List.of(new OrderItem(), new OrderItem())));
                    order.setUsername(username);
                    return order;
                });
        BDDMockito.willDoNothing().given(basketRepository).deleteById(ArgumentMatchers.anyInt());
        BDDMockito.willDoNothing().given(brokerMessage).sendMessage(ArgumentMatchers.any(RequestOrderDTO.class));

        //when
        RequestResponseDTO<RequestOrderDTO> requestResponseDTO = serviceOrder.createNewOrder(infoAboutOrderDTO, "Bearer-token");

        //then
        Assertions.assertThat(requestResponseDTO).isNotNull();
        Assertions.assertThat(requestResponseDTO.getData().getUsername()).isEqualTo("Russo");
        Assertions.assertThat(requestResponseDTO.getMessage()).isEqualTo("Order with id: 1 is saved.");
    }

    @DisplayName("Junit test for delete order operation.")
    @Test
    public void givenOrderID_whenDeleteOrder_thenReturnRequestResponseDTO(){
        //given
        Integer orderID = 1;
        Order order = new Order(
                1,
                "Username",
                new OrderInfo(
                        1,
                        "russo@mail.ru",
                        "Moscow, Tverskaya street, 6",
                        "89779993473",
                        List.of(new OrderItem(), new OrderItem()))
        );
        BDDMockito.given(orderRepository.findById(orderID)).willReturn(Optional.of(order));
        BDDMockito.willDoNothing().given(orderRepository).deleteById(orderID);
        //when
        RequestResponseDTO<String> requestResponseDTO = serviceOrder.deleteOrder(orderID);
        //then
        Assertions.assertThat(requestResponseDTO).isNotNull();
        Assertions.assertThat(requestResponseDTO.getMessage()).isEqualTo("Order with id: %s successfully delete.".formatted(orderID));
        Assertions.assertThat(requestResponseDTO.getData()).isEqualTo("SUCCESS.");
    }

}
