package com.nva.server.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nva.server.dtos.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidApiEndpointException.class)
    public ResponseEntity<?> handleInvalidApiEndpointException(InvalidApiEndpointException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExistedException.class)
    public ResponseEntity<?> handleUserExistedException(UserExistedException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(CustomAuthenticationException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<?> handlePasswordException(PasswordException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleCommonException(CommonException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.FORBIDDEN.value());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(objectMapper.convertValue(exceptionResponse, Map.class), HttpStatus.FORBIDDEN);
    }
}
