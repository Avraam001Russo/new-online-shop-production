package com.russozaripov.inventoryservice.service;

import com.russozaripov.inventoryservice.DTO.Supply_product_DTO;
import com.russozaripov.inventoryservice.event.DTO.OrderInfoDTO;
import com.russozaripov.inventoryservice.event.DTO.OrderItemDTO;
import com.russozaripov.inventoryservice.event.DTO.RequestOrderDTO;
import com.russozaripov.inventoryservice.model.InventoryModel;
import com.russozaripov.inventoryservice.repository.InventRepository;
import com.russozaripov.inventoryservice.service.inventoryService.InventoryService;
import com.russozaripov.inventoryservice.service.kafkaBroker.KafkaProducerService;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTests {

    @Mock
    private InventRepository inventRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @InjectMocks
    private InventoryService inventoryService;

    InventoryModel inventoryModel;
    @BeforeEach
    public void setUp(){
        inventoryModel = InventoryModel.builder()
                .id("1")
                .quantity(10)
                .skuCode("Samsung galaxy S22")
                .build();
    }

    @DisplayName("Junit test for create inventory model operation.")
    @Test
    public void givenSkuCode_whenCreateInventoryModel_thenReturnString(){
        //given
        String skuCode = "Samsung Galaxy S22";
        InventoryModel inventoryModel = InventoryModel.builder()
                .id(UUID.randomUUID().toString())
                .skuCode(skuCode).build();
        BDDMockito.given(inventRepository.save(ArgumentMatchers.any(InventoryModel.class))).willReturn(inventoryModel);
        //when
        String response = inventoryService.create_New_Inventory_Model(skuCode);
        //then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isEqualTo("inventoryModel %s successfully insert.".formatted(skuCode));
    }
    @DisplayName("Junit test for supply product operation.")
    @Test
    public void givenSupplyProductDTO_whenSupplyProduct_thenReturnString(){
        //given
        Supply_product_DTO product_dto = Supply_product_DTO.builder()
                .skuCode("Samsung galaxy S22")
                .quantity(100)
                .build();

        BDDMockito.given(inventRepository.findInventoryModelBySkuCode(product_dto.getSkuCode())).willReturn(Optional.of(inventoryModel));
        //when
        String response = inventoryService.supply_Product(product_dto);
        //then
        Mockito.verify(inventRepository, Mockito.times(1)).save(ArgumentMatchers.any(InventoryModel.class));
        Mockito.verify(kafkaProducerService, Mockito.times(1)).sendMessageToKafka(ArgumentMatchers.any(Supply_product_DTO.class));
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isEqualTo("inventory-model: %s successfully update.".formatted(product_dto.getSkuCode()));
    }
    @DisplayName("Junit test for received message operation.")
    @Test
    public void givenRequestOrderDTO_whenReceivedMessageFromBroker_thenReturnNothing(){
        //Given
        OrderItemDTO orderItemDTO_1 = new OrderItemDTO("Samsung galaxy S22", 1, 159990);
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>(List.of(orderItemDTO_1));
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO("russo@mail.ru", "Moscow, Tverskaya 6", "89779993473", orderItemDTOS);
        RequestOrderDTO requestOrderDTO = new RequestOrderDTO(1, orderInfoDTO, "RUSSO");
        BDDMockito.given(inventRepository.findInventoryModelBySkuCode(ArgumentMatchers.anyString())).willReturn(Optional.of(inventoryModel));
        //when
        inventoryService.receivedMessageFromOrderService(requestOrderDTO);
        //then
        Mockito.verify(inventRepository, Mockito.times(1)).save(ArgumentMatchers.any(InventoryModel.class));
        Assertions.assertThat(inventoryModel.getQuantity()).isEqualTo(9);

    }
}
