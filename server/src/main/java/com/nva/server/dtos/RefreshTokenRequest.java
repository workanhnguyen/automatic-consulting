package com.nva.server.dtos;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
