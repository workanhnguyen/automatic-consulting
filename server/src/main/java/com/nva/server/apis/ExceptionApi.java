package com.nva.server.apis;

import com.nva.server.exceptions.InvalidApiEndpointException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/**")
public class ExceptionApi {
    // Exception to invalid api endpoint
    @RequestMapping("")
    public ResponseEntity<?> handleInvalidApiRequest() {
        throw new InvalidApiEndpointException("Endpoint is not found.");
    }
}
