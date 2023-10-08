package com.russozaripov.orderservice.exceptionHandler.OrderException;

public class OrderException extends RuntimeException{
    public OrderException(String message) {
        super(message);
    }
}
