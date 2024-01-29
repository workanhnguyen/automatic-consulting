package com.nva.server.exceptions;

public class InvalidApiEndpointException extends RuntimeException {
    public InvalidApiEndpointException(String message) {
        super(message);
    }
}
