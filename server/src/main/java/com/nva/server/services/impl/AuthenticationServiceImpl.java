package com.nva.server.services.impl;

import com.nva.server.dtos.JwtAuthenticationResponse;
import com.nva.server.dtos.RefreshTokenRequest;
import com.nva.server.dtos.SignInRequest;
import com.nva.server.dtos.SignUpRequest;
import com.nva.server.entities.User;
import com.nva.server.exceptions.UserExistedException;
import com.nva.server.exceptions.UserNotFoundException;
import com.nva.server.repositories.UserRepository;
import com.nva.server.services.AuthenticationService;
import com.nva.server.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public User signup(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isEmpty()) {
            User user = new User();
            user.setEmail(signUpRequest.getEmail());
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

            return userRepository.save(user);
        } else {
            throw new UserExistedException("Email is taken.");
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Email hoặc mật khẩu không chính xác");
        }

        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password!"));
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var token = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(token);
            jwtAuthenticationResponse.setRefreshToken(jwtService.generateRefreshToken(new HashMap<>(), user));

            return jwtAuthenticationResponse;
        }
        return null;
    }
}
