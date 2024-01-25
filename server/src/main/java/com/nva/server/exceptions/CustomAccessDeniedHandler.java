package com.nva.server.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a simple Map for the JSON response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", response.getStatus());
        responseBody.put("message", "You don't have permission to access!");
        responseBody.put("timestamp", new Date());

        // Convert the map to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);

        // Set content type and write the JSON response
        response.setContentType("application/json");
        response.getWriter().write(jsonResponseBody);
    }
}
