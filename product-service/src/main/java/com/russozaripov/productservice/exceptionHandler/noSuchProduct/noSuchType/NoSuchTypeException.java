package com.russozaripov.productservice.exceptionHandler.noSuchProduct.noSuchType;

public class NoSuchTypeException extends RuntimeException{
    public NoSuchTypeException(String message) {
        super(message);
    }
}
