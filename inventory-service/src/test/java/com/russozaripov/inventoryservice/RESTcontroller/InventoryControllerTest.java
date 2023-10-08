package com.russozaripov.inventoryservice.RESTcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.russozaripov.inventoryservice.DTO.Supply_product_DTO;
import com.russozaripov.inventoryservice.service.inventoryService.InventoryService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
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
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private InventoryService inventoryService;


    @DisplayName("Junit test for create new inventory model operation.")
    @Test
    public void givenSkuCode_whenCreateNewInventoryModel_thenReturnString() throws Exception {
        //given
        String skuCode = "Samsung Galaxy S22";
        String response = "inventoryModel %s successfully insert.".formatted(skuCode);
        BDDMockito.given(inventoryService.create_New_Inventory_Model(ArgumentMatchers.anyString())).willReturn(response);
        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skuCode))
        );
        //theb
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.is(response)));
    }

    @DisplayName("Junit test for supply product operation.")
    @Test
    public void givenSupplyProductDTO_whenSupplyProductToStock_thenReturnString() throws Exception {
        //given
        Supply_product_DTO product_dto = Supply_product_DTO.builder()
                .skuCode("Samsung galaxy S22")
                .quantity(100)
                .build();
        String response = "inventory-model: %s successfully update.".formatted(product_dto.getSkuCode());
        BDDMockito.given(inventoryService.supply_Product(ArgumentMatchers.any(Supply_product_DTO.class))).willReturn(response);
        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/supply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product_dto))
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.is(response)));
    }
}
