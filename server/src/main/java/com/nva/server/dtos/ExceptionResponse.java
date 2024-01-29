package com.nva.server.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponse {
    private int status;
    private String message;
    private Date timestamp;
}
