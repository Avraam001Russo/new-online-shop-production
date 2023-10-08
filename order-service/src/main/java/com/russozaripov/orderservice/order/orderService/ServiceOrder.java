package com.russozaripov.orderservice.order.orderService;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.basket.model.ProductInBasket;
import com.russozaripov.orderservice.basket.repository.BasketRepository;
import com.russozaripov.orderservice.exceptionHandler.OrderException.OrderException;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.OrderInfoDTO;
import com.russozaripov.orderservice.order.DTO.OrderItemDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import com.russozaripov.orderservice.order.orderService.messageBroker.BrokerMessage;
import com.russozaripov.orderservice.order.model.OrderInfo;
import com.russozaripov.orderservice.order.model.OrderItem;
import com.russozaripov.orderservice.order.model.Order;
import com.russozaripov.orderservice.order.orderRepository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ServiceOrder {
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final BrokerMessage brokerMessage;
    private final ServiceJWT serviceJWT;

    public RequestResponseDTO<RequestOrderDTO> createNewOrder(InfoAboutOrderDTO infoAboutOrderDTO, String authorization){

        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);

        String DELIVERY_ADDRESS = infoAboutOrderDTO.getDeliveryAddress();
        String EMAIL = infoAboutOrderDTO.getEmail();
        String PHONE_NUMBER = infoAboutOrderDTO.getPhoneNumber();
        if (DELIVERY_ADDRESS != null && EMAIL != null && PHONE_NUMBER != null){

        OrderInfo orderInfo = OrderInfo.builder().deliveryAddress(DELIVERY_ADDRESS).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        Order order = Order.builder().username(username).orderInfo(orderInfo).build();

        Basket basket = basketRepository.findBasketByUsername(username).get();
        List<ProductInBasket> listOfProducts = (List<ProductInBasket>) basket.getProductsInBaskets();

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (ProductInBasket productInBasket : listOfProducts){
            String SKU_CODE = productInBasket.getSkuCode();
            int QUANTITY = productInBasket.getQuantity();
            int PRICE = productInBasket.getPrice();
        OrderItem orderItem = OrderItem.builder().quantity(QUANTITY).price(PRICE).sku_Code(SKU_CODE).build();
        OrderItemDTO orderItemDTO = OrderItemDTO.builder().sku_Code(SKU_CODE).price(PRICE).quantity(QUANTITY).build();
            orderItemDTOList.add(orderItemDTO);
            orderInfo.addOrderItemsInOrderInfo(orderItem);
        }

                Order savedOrder = orderRepository.save(order);
                log.info("Order saved.{}", savedOrder.getUsername());
                basketRepository.deleteById(basket.getId());
                log.info("Basket %s successfully delete after create order.".formatted(savedOrder.getUsername()));
        OrderInfoDTO orderInfoDTO = OrderInfoDTO.builder()
                .deliveryAddress(DELIVERY_ADDRESS)
                .email(EMAIL)
                .phoneNumber(PHONE_NUMBER)
                .orderItemDTOCollection(orderItemDTOList)
                .build();
        RequestOrderDTO requestOrderDTO = RequestOrderDTO.builder()
                .orderID(savedOrder.getId())
                .username(username)
                .orderInfoDTO(orderInfoDTO)
                .build();

                brokerMessage.sendMessage(requestOrderDTO);
                log.info("order-service sent message -> " + requestOrderDTO);

        return new RequestResponseDTO<>(requestOrderDTO, "Order with id: %s is saved.".formatted(order.getId()));
        }
        else {
            throw new OrderException("Invalid data order. Please, enter all required information.");
        }
    }

    public RequestResponseDTO<String> deleteOrder(Integer orderID) {
        Optional<Order> orderOptional = orderRepository.findById(orderID);
        if (orderOptional.isPresent()){
            orderRepository.deleteById(orderID);
            String successDelete = "Order with id: %s successfully delete.".formatted(orderID);
            log.info(successDelete);
            return new RequestResponseDTO<>("SUCCESS.", successDelete);
        } else {
            return new RequestResponseDTO<>("ORDER_NOT_FOUND_EXCEPTION", "Order with id: %s not found".formatted(orderID));
        }
    }
}
