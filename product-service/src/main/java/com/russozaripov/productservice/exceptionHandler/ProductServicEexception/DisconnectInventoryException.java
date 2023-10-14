package com.russozaripov.productservice.exceptionHandler.ProductServicEexception;

public class DisconnectInventoryException extends RuntimeException {
    public DisconnectInventoryException(String message) {
        super(message);
    }
}
