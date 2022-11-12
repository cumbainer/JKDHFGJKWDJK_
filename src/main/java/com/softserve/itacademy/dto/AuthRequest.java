package com.softserve.itacademy.dto;


import lombok.Data;
@Data
public class AuthRequest {
    private String username;/*email*/

    private String password;
}
