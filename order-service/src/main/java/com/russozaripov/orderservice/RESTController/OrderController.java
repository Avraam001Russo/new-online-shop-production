package com.russozaripov.orderservice.RESTController;

import com.russozaripov.orderservice.JWTParser.ServiceJWT;
import com.russozaripov.orderservice.basket.BasketService.ServiceBasket;
import com.russozaripov.orderservice.basket.DTO.BasketDTO;
import com.russozaripov.orderservice.basket.DTO.requestResponse.RequestResponseDTO;
import com.russozaripov.orderservice.basket.model.Basket;
import com.russozaripov.orderservice.order.DTO.InfoAboutOrderDTO;
import com.russozaripov.orderservice.order.DTO.RequestOrderDTO;
//import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import com.russozaripov.orderservice.order.orderService.ServiceOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
@EnableCaching
public class OrderController {

        private final ServiceOrder serviceOrder;
        private final ServiceBasket serviceBasket;
        private final ServiceJWT serviceJWT;
    @PostMapping("/createNewOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody InfoAboutOrderDTO infoAboutOrderDTO, @RequestHeader("Authorization") String authorization){
        RequestResponseDTO<RequestOrderDTO> newOrder = serviceOrder.createNewOrder(infoAboutOrderDTO, authorization);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteOrder/{orderID}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteOrder(@PathVariable("orderID") Integer orderID){
        RequestResponseDTO<String> response = serviceOrder.deleteOrder(orderID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/deleteBasket")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBasket(@RequestHeader("Authorization") String authorization){
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        serviceBasket.deleteBasket(username);
        return new ResponseEntity<>(new RequestResponseDTO<>("Success", "Basket deleted successfully."), HttpStatus.OK);
    }
    @PostMapping("/createBasket")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBasket(@RequestHeader("Authorization") String authorization, @RequestBody BasketDTO basketDTO){
        Basket basket = serviceBasket.createBasket(basketDTO, authorization);
        return new ResponseEntity<>(new RequestResponseDTO<>(basket, "Success."), HttpStatus.CREATED);

    }
    @PutMapping("/updateBasket")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateBasket(@RequestBody BasketDTO basketDTO, @RequestHeader("Authorization") String authorization){
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        Basket result = serviceBasket.updateBasket(basketDTO, username);
        return new ResponseEntity<>(new RequestResponseDTO<>(result, "Success."), HttpStatus.OK);
    }
    @GetMapping("/getAllBaskets")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllBaskets(){
        List<Basket> basketList = serviceBasket.getAllBaskets();
        return new ResponseEntity<>(new RequestResponseDTO<>(basketList, "Success."), HttpStatus.OK);
    }
    @GetMapping("/getSingleBasket")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBasketById(@RequestHeader("Authorization") String authorization){
        String token = authorization.substring(7);
        String username = serviceJWT.getUserName(token);
        Basket basket = serviceBasket.getSingleBasket(username);
        return ResponseEntity.ok(new RequestResponseDTO<>(basket, "Success."));
    }

}

