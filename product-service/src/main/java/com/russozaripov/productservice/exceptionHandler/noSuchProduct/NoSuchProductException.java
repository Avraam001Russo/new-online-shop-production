package com.russozaripov.productservice.exceptionHandler.noSuchProduct;

public class NoSuchProductException extends RuntimeException{
    public NoSuchProductException(String message) {
        super(message);
    }
}
