package com.russozaripov.orderservice.order;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import com.russozaripov.orderservice.order.model.Order;
import com.russozaripov.orderservice.order.orderRepository.OrderRepository;
import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import com.russozaripov.orderservice.order.orderService.messageBroker.BrokerMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceOrderTests {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BrokerMessage brokerMessage;
    @Mock
    private ServiceJWT serviceJWT;
    @Mock
    private ServiceBasket serviceBasket;
    @InjectMocks
    private ServiceOrder serviceOrder;

    @DisplayName("Junit test for create order operation.")
    @Test
    public void givenOrderDTO_whenCreateOrder_thenReturn(){
        Basket basket = new Basket();
        ProductInBasket productInBasket = new ProductInBasket(1, "Apple", 34889, 1, basket);
        ProductInBasket productInBasket_2 = new ProductInBasket(2, "Samsung", 26990, 2, basket);
        basket.setUsername("username");
        basket.setId(1);
        basket.addProductToBasket(productInBasket);
        basket.addProductToBasket(productInBasket_2);
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn("username");
        BDDMockito.given(serviceBasket.getSingleBasket(ArgumentMatchers.anyString())).willReturn(basket);
        BDDMockito.given(orderRepository.save(ArgumentMatchers.any(Order.class))).willAnswer((invocation)->{
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });
        InfoAboutOrderDTO infoAboutOrderDTO = InfoAboutOrderDTO.builder()
                .deliveryAddress("delivery address")
                .email("user@mail.com")
                .phoneNumber("89779993473")
                .build();
        BDDMockito.willDoNothing().given(brokerMessage).sendMessage(ArgumentMatchers.any(RequestOrderDTO.class));
        RequestResponseDTO<RequestOrderDTO> response = serviceOrder.createNewOrder(infoAboutOrderDTO, "Authorization");
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getData().getUsername()).isEqualTo("username");
        Assertions.assertThat(response.getData().getOrderID()).isEqualTo(1);


    }
    @DisplayName("Junit test for delete order operation.")
    @Test
    public void givenOrderId_whenDeleteOrder_thenReturnNothing(){
        Integer orderId = 1;
        BDDMockito.given(orderRepository.findById(ArgumentMatchers.anyInt())).willReturn(Optional.of(Order.builder().build()));
        BDDMockito.willDoNothing().given(orderRepository).deleteById(ArgumentMatchers.anyInt());
        RequestResponseDTO<String> response = serviceOrder.deleteOrder(orderId);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getData()).isEqualTo("SUCCESS.");
        Mockito.verify(orderRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
    }
}
