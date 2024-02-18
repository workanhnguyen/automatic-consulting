package com.nva.server.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomDialogflowResponse implements Serializable {
//    private Map<String, Object> parameters;
//    private String fulfillmentText;
//    private String intentDisplayName;
    private String responseText;
    private Long createdDate;
}
