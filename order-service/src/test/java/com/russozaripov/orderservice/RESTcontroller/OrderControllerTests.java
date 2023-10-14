package com.russozaripov.orderservice.RESTcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.OrderInfoDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.http.HttpHeaders;
import java.util.Collections;
import java.util.List;

@WebMvcTest
public class OrderControllerTests {

    @MockBean
    private ServiceOrder serviceOrder;
    @MockBean
    private ServiceBasket serviceBasket;
    @MockBean
    private ServiceJWT serviceJWT;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    BasketDTO basketDTO;
    Basket basket;
    String bearerToken;
    @BeforeEach
    public void setUp(){
         bearerToken = "some-bearer-token";
         basketDTO = BasketDTO.builder().basketItemDTO(Collections.emptyList()).build();
         basket = new Basket(1, "username", Collections.emptyList());
    }
    @DisplayName("Junit test for create basket operation.")
    @Test
    public void givenBasketDTO_whenCreateBasketOperation_thenBasketObject()throws Exception{
        BDDMockito.given(serviceBasket.createBasket(ArgumentMatchers.any(BasketDTO.class), ArgumentMatchers.anyString()))
                .willReturn(basket);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/createBasket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basketDTO))
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username", CoreMatchers.is("username")));
    }

    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenBasketDTO_whenUpdateBasketOperation_thenBasketObject()throws Exception{
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn("username");
        BDDMockito.given(serviceBasket.updateBasket(ArgumentMatchers.any(BasketDTO.class), ArgumentMatchers.anyString()))
                .willReturn(basket);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/order/updateBasket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basketDTO))
                .header("Authorization", bearerToken)
        );

        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username", CoreMatchers.is("username")));
    }
    @DisplayName("Junit test for get all baskets operation.")
    @Test
    public void givenListOfBasket_whenGetAllBasketsOperation_thenReturnBasketList()throws Exception{
        BDDMockito.given(serviceBasket.getAllBaskets()).willReturn(List.of(basket, new Basket(2, "username-2", Collections.emptyList())));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/order/getAllBaskets")
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Success.")));

    }

    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenBasketObject_whenGetSingleBasketById_thenBasketObject()throws Exception{
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn("username");
        BDDMockito.given(serviceBasket.getSingleBasket(ArgumentMatchers.anyString())).willReturn(basket);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/order/getSingleBasket")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Success.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username", CoreMatchers.is("username")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(1)));
    }
    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenUsername_whenDeleteBasket_thenReturnResponseEntity()throws Exception{
        BDDMockito.given(serviceJWT.getUserName(ArgumentMatchers.anyString())).willReturn("username");
        BDDMockito.willDoNothing().given(serviceBasket).deleteBasket(ArgumentMatchers.anyString());
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/order/deleteBasket")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Basket deleted successfully.")));
    }

    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenOrderDTO_whenCreateOrder_thenReturnOrderObject()throws Exception{
        RequestOrderDTO requestOrderDTO = RequestOrderDTO.builder()
                .orderID(1)
                .username("username")
                .orderInfoDTO(new OrderInfoDTO("@russo@mail.ru", "Moscow, Tverskaya 6", "89779993472", Collections.emptyList()))
                .build();
        BDDMockito.given(serviceOrder.createNewOrder(ArgumentMatchers.any(InfoAboutOrderDTO.class), ArgumentMatchers.anyString()))
                .willReturn(new RequestResponseDTO<>(requestOrderDTO, "Success"));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/createNewOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InfoAboutOrderDTO("russo@email.ru",
                        "Moscow, Tverskaya 6",
                        "89779993473")))
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderID", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username", CoreMatchers.is("username")));

    }
    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenOrderId_whenDeleteOrder_thenReturnResponseEntity()throws Exception{
        Integer orderId = 1;
     BDDMockito.given(serviceOrder.deleteOrder(ArgumentMatchers.anyInt())).willReturn(new RequestResponseDTO<>("Success", "Order deleted successfully"));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/order/deleteOrder/{orderID}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Order deleted successfully")));
    }

}
