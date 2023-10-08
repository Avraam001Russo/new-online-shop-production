package com.russozaripov.inventoryservice.service.kafkaBroker;

import com.russozaripov.inventoryservice.DTO.Supply_product_DTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final String TOPIC_1 = "inventory-topic";
    private final KafkaTemplate<String, Supply_product_DTO> kafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);
    public void sendMessageToKafka(Supply_product_DTO supply_product_dto){
        LOGGER.info("sent message: %s".formatted(supply_product_dto));
        kafkaTemplate.send(TOPIC_1, supply_product_dto);
    }
}
