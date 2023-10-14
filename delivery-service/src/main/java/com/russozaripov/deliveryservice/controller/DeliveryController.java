package com.russozaripov.deliveryservice.controller;

import com.russozaripov.deliveryservice.deliveryService.ServiceDelivery;
import com.russozaripov.deliveryservice.model.DeliveryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class DeliveryController {

    @MessageMapping("/delivery-public")
    @SendTo("/delivery/chatroom")
    public DeliveryModel deliveryMessage(DeliveryModel deliveryModel){
        return deliveryModel;
    }
}
