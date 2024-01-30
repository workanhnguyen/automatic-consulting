package com.nva.server.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nva.server.dtos.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class CustomExceptionHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handle(HttpServletResponse response, String message) throws IOException {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(response.getStatus());
        exceptionResponse.setMessage(message);
        exceptionResponse.setTimestamp(new Date().getTime());

        // Convert the map to JSON string
        String jsonResponseBody = objectMapper.writeValueAsString(objectMapper.convertValue(exceptionResponse, Map.class));

        // Set content type and write the JSON response
        response.setContentType("application/json");
        response.getWriter().write(jsonResponseBody);
    }
}
