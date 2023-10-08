package com.russozaripov.orderservice.exceptionHandler.GlobalException;

import com.russozaripov.orderservice.exceptionHandler.JWTException.JwtTokenIncorrectData;
import com.russozaripov.orderservice.exceptionHandler.JWTException.NoValidJWTException;
import com.russozaripov.orderservice.exceptionHandler.OrderException.IncorrectDataOrderException;
import com.russozaripov.orderservice.exceptionHandler.OrderException.OrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JwtTokenIncorrectData> incorrectJWT(NoValidJWTException noValidJWTException){
        JwtTokenIncorrectData incorrectData = new JwtTokenIncorrectData();
        incorrectData.setInfo(noValidJWTException.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<IncorrectDataOrderException> incorrectDataOrderException(OrderException orderException){
        IncorrectDataOrderException incorrectDataOrderException = new IncorrectDataOrderException();
        incorrectDataOrderException.setInfo(orderException.getMessage());
        return new ResponseEntity<>(incorrectDataOrderException, HttpStatus.BAD_REQUEST);
    }
}
