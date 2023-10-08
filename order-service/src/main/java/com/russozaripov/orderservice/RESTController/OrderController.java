package com.russozaripov.orderservice.RESTController;

import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

        private final ServiceOrder serviceOrder;
        private final ServiceBasket serviceBasket;
    @PostMapping("/createNewOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody InfoAboutOrderDTO infoAboutOrderDTO, @RequestHeader("Authorization") String authorization){
        RequestResponseDTO<RequestOrderDTO> requestResponseDTO = serviceOrder.createNewOrder(infoAboutOrderDTO, authorization);
        return new ResponseEntity<>(requestResponseDTO, HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteOrder/{orderID}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteOrder(@PathVariable("orderID") Integer orderID){
        RequestResponseDTO<String> requestResponseDTO = serviceOrder.deleteOrder(orderID);
        return new ResponseEntity<>(requestResponseDTO, HttpStatus.OK);
    }


    @PutMapping("/deleteProductInBasket")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteProductInBasket(@RequestHeader("Authorization") String authorization, @RequestParam(value = "skuCode") String skuCode){
        RequestResponseDTO<String> requestResponseDTO = serviceBasket.deleteProduct(skuCode, authorization);
        return new ResponseEntity<>(requestResponseDTO, HttpStatus.OK);
    }
    @PostMapping("/createBasket")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBasket(@RequestHeader("Authorization") String authorization, @RequestBody BasketDTO basketDTO){
        RequestResponseDTO<String> requestResponseDTO = serviceBasket.createBasket(basketDTO, authorization);
        return new ResponseEntity<>(requestResponseDTO, HttpStatus.CREATED);
    }
    @PutMapping("/updateBasket")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateBasket(@RequestBody BasketDTO basketDTO, @RequestHeader("Authorization") String authorization){
        RequestResponseDTO<String> requestResponseDTO = serviceBasket.updateBasket(basketDTO, authorization);
        return new ResponseEntity<>(requestResponseDTO, HttpStatus.OK);
    }

}

