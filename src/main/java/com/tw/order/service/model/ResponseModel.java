package com.tw.order.service.model;

import com.tw.order.controller.dto.ResponseBody;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseModel<T> {
    public ResponseModel(HttpStatus status, ResponseBody details) {
        this.status = status;
        this.details = details;
    }
    private HttpStatus status;
    private ResponseBody<T> details;
}
