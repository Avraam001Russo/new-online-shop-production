package com.russozaripov.emailnotificationservice.service;

import com.russozaripov.emailnotificationservice.event.DTO.OrderInfoDTO;
import com.russozaripov.emailnotificationservice.event.DTO.OrderItemDTO;
import com.russozaripov.emailnotificationservice.event.DTO.RequestOrderDTO;
import com.russozaripov.emailnotificationservice.mailService.ServiceMail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTests {
    @Mock
    private ServiceMail serviceMail;
    @InjectMocks
    private NotificationService notificationService;

    @DisplayName("Junit test for new order event operation.")
    @Test
    public void givenRequestOrderDTO_whenNewOrderEvent_thenReturnNothing(){
        //Given
        OrderItemDTO orderItemDTO_1 = new OrderItemDTO("Samsung galaxy S22", 1, 159990);
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>(List.of(orderItemDTO_1));
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO("russo@mail.ru", "Moscow, Tverskaya 6", "89779993473", orderItemDTOS);
        RequestOrderDTO requestOrderDTO = new RequestOrderDTO(1, orderInfoDTO, "RUSSO");
        //when
        notificationService.new_Order_Event_Message(requestOrderDTO);
        //then
        Mockito.verify(serviceMail, Mockito.times(1))
                .sendMessage(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }
}
