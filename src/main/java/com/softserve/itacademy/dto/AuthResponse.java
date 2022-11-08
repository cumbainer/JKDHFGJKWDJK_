package com.softserve.itacademy.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String username;
    private String token;
}
