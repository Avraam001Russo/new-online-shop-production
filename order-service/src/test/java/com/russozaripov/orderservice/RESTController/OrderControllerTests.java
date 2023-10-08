package com.russozaripov.orderservice.RESTController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest
public class OrderControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ServiceBasket serviceBasket;
    @MockBean
    private ServiceOrder serviceOrder;



    String bearerToken;
    @BeforeEach
    public void setUp(){
        bearerToken = "some-Bearer-token";
    }

    @DisplayName("Junit test for create basket operation.")
    @Test
    public void givenBasketDTO_whenCreateBasket_ThenReturnResponseEntity() throws Exception {
        BasketDTO basketDTO = new BasketDTO();
        RequestResponseDTO<String> requestResponseDTO = new RequestResponseDTO<>("Success.", "Basket successfully save");
        BDDMockito.given(serviceBasket.createBasket(ArgumentMatchers.any(BasketDTO.class), ArgumentMatchers.anyString()))
                .willReturn(requestResponseDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/createBasket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basketDTO))
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("Success.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Basket successfully save")));
    }
    @DisplayName("Junit test for create order operation.")
    @Test
    public void givenInfoAboutOrderDTO_whenCreateOrder_thenReturnResponseEntity() throws Exception {
        RequestOrderDTO requestOrderDTO = new RequestOrderDTO();
        InfoAboutOrderDTO infoAboutOrderDTO = new InfoAboutOrderDTO();
        RequestResponseDTO<RequestOrderDTO> requestResponseDTO = new RequestResponseDTO<>(requestOrderDTO, "Order successfully saved.");
        BDDMockito.given(serviceOrder.createNewOrder(ArgumentMatchers.any(InfoAboutOrderDTO.class), ArgumentMatchers.anyString()))
                .willReturn(requestResponseDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/createNewOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoAboutOrderDTO))
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Order successfully saved.")));
    }
    @DisplayName("Junit test for update basket operation.")
    @Test
    public void givenBasketDTO_whenUpdateBasket_thenReturnResponseEntity() throws Exception {
            RequestResponseDTO<String> requestResponseDTO = new RequestResponseDTO<>("Basket successfully update.", "Success.");
            BasketDTO basketDTO = new BasketDTO();
            BDDMockito.given(serviceBasket.updateBasket(ArgumentMatchers.any(BasketDTO.class), ArgumentMatchers.anyString()))
                .willReturn(requestResponseDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/order/updateBasket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketDTO))
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("Basket successfully update.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Success.")));
    }
    @DisplayName("Junit test for delete product from basket operation.")
    @Test
    public void givenSkuCode_whenDeleteProductFromBasket_thenReturnResponseEntity() throws Exception {
        RequestResponseDTO<String> requestResponseDTO = new RequestResponseDTO<>("Product in basket successfully delete.", "Success.");
        String skuCode = "skuCode";
        BDDMockito.given(serviceBasket.deleteProduct(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .willReturn(requestResponseDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/order/deleteProductInBasket?skuCode=" + skuCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", bearerToken)
                );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("Product in basket successfully delete.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Success.")));
    }
    @DisplayName("Junit test for delete order operation.")
    @Test
    public void givenOrderId_whenDeleteOrder_thenReturnResponseEntity() throws Exception {
        Integer orderID = 1;
        String successDeleteMessage = "Order with id: %s successfully delete.".formatted(orderID);
        RequestResponseDTO<String> requestResponseDTO = new RequestResponseDTO<>("SUCCESS.", successDeleteMessage);
        BDDMockito.given(serviceOrder.deleteOrder(orderID)).willReturn(requestResponseDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/order/deleteOrder/{orderID}", orderID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", bearerToken)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is("SUCCESS.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(successDeleteMessage)));
    }
}
