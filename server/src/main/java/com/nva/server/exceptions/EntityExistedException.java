package com.nva.server.exceptions;

public class EntityExistedException extends RuntimeException {
    public EntityExistedException(String message) {
        super(message);
    }
}
