package com.russozaripov.orderservice.basket.DTO.requestResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RequestResponseDTO<T> implements RequestResponse<T> {
    private T data;
    private String message;

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
