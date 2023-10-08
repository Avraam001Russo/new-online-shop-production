package com.russozaripov.orderservice.order.orderService.messageBroker;

import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrokerMessage {
    private final KafkaTemplate<String, RequestOrderDTO> kafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(BrokerMessage.class);
    public void sendMessage(RequestOrderDTO requestOrderDTO){
        kafkaTemplate.send("newOrder", requestOrderDTO);
        LOGGER.info("Broker sent message -> %s".formatted(requestOrderDTO.toString()));
    }
}
