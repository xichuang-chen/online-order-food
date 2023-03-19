package com.tw.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBody<T> {

    public ResponseBody(String message) {
        this.message = message;
    }
    private String message;
    private T data;
}
