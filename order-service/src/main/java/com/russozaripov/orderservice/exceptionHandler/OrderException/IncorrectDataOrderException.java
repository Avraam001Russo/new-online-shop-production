package com.russozaripov.orderservice.exceptionHandler.OrderException;

public class IncorrectDataOrderException {
    private String info;

    public IncorrectDataOrderException(String info) {
        this.info = info;
    }
    public IncorrectDataOrderException() {
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
