package com.russozaripov.inventoryservice.service.messageBroker;

import com.russozaripov.inventoryservice.DTO.Supply_product_DTO;
import com.russozaripov.inventoryservice.service.kafkaBroker.KafkaProducerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

@ExtendWith({MockitoExtension.class})
public class KafkaProducerServiceTests {
    @Mock
    private KafkaTemplate<String, Supply_product_DTO> kafkaTemplate;
    @Mock
    private Logger logger;
    @InjectMocks
    private KafkaProducerService producerService;
    @DisplayName("Junit test for send message operation.")
    @Test
    public void givenRequestOrderDTO_whenSendMessage_thenReturnNothing(){
        //given
        Supply_product_DTO product_dto = Supply_product_DTO.builder()
                .skuCode("Samsung galaxy S22")
                .quantity(100)
                .build();

        // When
        producerService.sendMessageToKafka(product_dto);
        // Then
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(ArgumentMatchers.anyString(), ArgumentMatchers.any(Supply_product_DTO.class));
    }
}
