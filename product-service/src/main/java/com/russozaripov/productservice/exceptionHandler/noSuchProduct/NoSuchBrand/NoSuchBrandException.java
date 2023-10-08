package com.russozaripov.productservice.exceptionHandler.noSuchProduct.NoSuchBrand;

public class NoSuchBrandException extends RuntimeException{
    public NoSuchBrandException(String message) {
        super(message);
    }
}
