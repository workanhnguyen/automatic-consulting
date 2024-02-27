package com.nva.server.services.impl;

import com.nva.server.dtos.JwtAuthenticationResponse;
import com.nva.server.dtos.RefreshTokenRequest;
import com.nva.server.dtos.SignInRequest;
import com.nva.server.dtos.SignUpRequest;
import com.nva.server.entities.User;
import com.nva.server.exceptions.CommonException;
import com.nva.server.exceptions.InvalidTokenException;
import com.nva.server.exceptions.UserExistedException;
import com.nva.server.exceptions.UserNotFoundException;
import com.nva.server.repositories.UserRepository;
import com.nva.server.services.AuthenticationService;
import com.nva.server.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public void signup(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isEmpty()) {
            User user = new User();
            user.setEmail(signUpRequest.getEmail());
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

            userRepository.save(user);
        } else {
            throw new UserExistedException("Email đã được sử dụng.");
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
        try {
            Optional<User> user = userRepository.findByEmail(signInRequest.getEmail());
            if (user.isPresent()) {
                if (user.get().getIsEnabled())
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
                else
                    throw new UserNotFoundException("Tài khoản đã bị khóa");
            }
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
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshToken != null && !refreshToken.isEmpty()) {
            try {
                String userEmail = jwtService.extractUsernameV2(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    String token = jwtService.generateToken(userDetails);

                    JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
                    jwtAuthenticationResponse.setToken(token);
                    jwtAuthenticationResponse.setRefreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetails));

                    return jwtAuthenticationResponse;
                }
            } catch (Exception e) {
                throw new InvalidTokenException("Refresh token is invalid or expired.");
            }
        }
        throw new CommonException("Refresh token is null or empty.");
    }
}
