package com.russozaripov.deliveryservice.deliveryService;

import com.russozaripov.deliveryservice.DTO.OrderItemDTO;
import com.russozaripov.deliveryservice.DTO.RequestOrderDTO;
import com.russozaripov.deliveryservice.model.DeliveryModel;
import com.russozaripov.deliveryservice.model.OrderItem;
import com.russozaripov.deliveryservice.repository.DeliveryModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceDelivery {
    private final DeliveryModelRepository deliveryModelRepository;
    @KafkaListener(topics = "newOrder", groupId = "delivery-group")
    public void receivedMessageWithNewOrder(RequestOrderDTO requestOrderDTO){
        DeliveryModel deliveryModel = DeliveryModel.builder()
                .orderId(requestOrderDTO.getOrderID())
                .deliveryPrice(10)
                .deliveryAddress(requestOrderDTO.getOrderInfoDTO().getDeliveryAddress())
                .email(requestOrderDTO.getOrderInfoDTO().getEmail())
                .phoneNumber(requestOrderDTO.getOrderInfoDTO().getPhoneNumber())
                .username(requestOrderDTO.getUsername())
                .build();
        List<OrderItem> listOfrderItems = new ArrayList<>();
        for (OrderItemDTO dto : requestOrderDTO.getOrderInfoDTO().getOrderItemDTOCollection()){
            OrderItem orderItem = OrderItem.builder()
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .skuCode(dto.getSku_Code())
                    .ordersToDelivery(deliveryModel)
                    .build();
            listOfrderItems.add(orderItem);
        }
        deliveryModel.setListOfOrderItem(listOfrderItems);
        deliveryModelRepository.save(deliveryModel);
        log.info("Delivery model saved: %s".formatted(deliveryModel.toString()));
    }
}
