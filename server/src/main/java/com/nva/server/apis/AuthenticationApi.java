package com.nva.server.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nva.server.dtos.*;
import com.nva.server.entities.User;
import com.nva.server.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        try {
            return ResponseEntity.ok(authenticationService.signin(signInRequest));
        } catch (IllegalArgumentException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse();
            exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            exceptionResponse.setMessage(e.getMessage());
            exceptionResponse.setTimestamp(new Date());

            ObjectMapper objectMapper = new ObjectMapper();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(objectMapper.convertValue(exceptionResponse, Map.class));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}
