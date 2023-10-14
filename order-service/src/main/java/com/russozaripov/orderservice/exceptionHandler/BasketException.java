package com.russozaripov.orderservice.exceptionHandler;

public class BasketException extends RuntimeException{
    public BasketException(String message) {
        super(message);
    }
}
