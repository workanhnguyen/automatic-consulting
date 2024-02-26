package com.nva.server.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomDialogflowResponse implements Serializable {
    private String question;
    private String answer;
    private Long createdDate;
}
