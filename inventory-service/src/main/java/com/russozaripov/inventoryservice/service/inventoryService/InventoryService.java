package com.russozaripov.inventoryservice.service.inventoryService;

import com.russozaripov.inventoryservice.DTO.Supply_product_DTO;
import com.russozaripov.inventoryservice.event.DTO.OrderItemDTO;
import com.russozaripov.inventoryservice.event.DTO.RequestOrderDTO;
import com.russozaripov.inventoryservice.model.InventoryModel;
import com.russozaripov.inventoryservice.repository.InventRepository;
import com.russozaripov.inventoryservice.service.kafkaBroker.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventRepository inventRepository;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    public String create_New_Inventory_Model(String skuCode) {
        InventoryModel inventoryModel = InventoryModel.builder()
                .id(UUID.randomUUID().toString())
                .skuCode(skuCode).build();
        InventoryModel savedInventory = inventRepository.save(inventoryModel);
        log.info(inventoryModel.toString());
        return "inventoryModel %s successfully insert.".formatted(savedInventory.getSkuCode());
    }

    public String supply_Product(Supply_product_DTO supply_product_dto){
        InventoryModel inventoryModel = inventRepository.findInventoryModelBySkuCode(supply_product_dto.getSkuCode()).get();
        inventoryModel.setQuantity(supply_product_dto.getQuantity());
        inventRepository.save(inventoryModel);
        log.info(inventoryModel.toString());
        kafkaProducerService.sendMessageToKafka(supply_product_dto);
        return "inventory-model: %s successfully update.".formatted(supply_product_dto.getSkuCode());
    }
    @KafkaListener(topics = "newOrder", groupId = "inventory-group")
    public void receivedMessageFromOrderService(RequestOrderDTO requestOrderDTO){
        log.info("Received message -> new Order %s".formatted(requestOrderDTO.getOrderID()));
        for (OrderItemDTO itemDTO : requestOrderDTO.getOrderInfoDTO().getOrderItemDTOCollection()){
            Optional<InventoryModel> inventoryModelBySkuCode = inventRepository.findInventoryModelBySkuCode(itemDTO.getSku_Code());
            if (inventoryModelBySkuCode.isPresent()){
                InventoryModel inventoryModel = inventoryModelBySkuCode.get();
                log.info("Quantity before update: %s".formatted(inventoryModel.getQuantity()));
                int inventoryBalance = inventoryModel.getQuantity() - itemDTO.getQuantity();
                inventoryModel.setQuantity(inventoryBalance);

                if(inventoryBalance == 0){
                    Supply_product_DTO product_dto = Supply_product_DTO.builder()
                                    .quantity(inventoryBalance)
                                            .skuCode(inventoryModel.getSkuCode())
                                                    .build();
                    kafkaProducerService.sendMessageToKafka(product_dto);
                }
                inventRepository.save(inventoryModel);
                log.info("Inventory balance '%s' update. CURRENT QUANTITY -> %d".formatted(inventoryModel.getSkuCode(), inventoryBalance));
            }
            else {
                throw new IllegalArgumentException("Inventory model '%s' not found.".formatted(itemDTO.getSku_Code()));
            }
        }

    }


}
