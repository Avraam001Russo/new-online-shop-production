package com.russozaripov.orderservice.exceptionHandler.JWTException;

public class JwtTokenIncorrectData {
    private String info;

    public JwtTokenIncorrectData(String info) {
        this.info = info;
    }

    public JwtTokenIncorrectData() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
