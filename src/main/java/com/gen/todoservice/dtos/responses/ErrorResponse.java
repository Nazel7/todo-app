package com.gen.todoservice.dtos.responses;

import lombok.Data;

@Data
public class ErrorResponse {

    private String status;
    private String message;
    private Long timeStamp;
}
