package com.softserve.itacademy.Exception;

import org.springframework.security.core.AuthenticationException;

public class JWT_AUTH_EXCEPTION extends AuthenticationException {

    public JWT_AUTH_EXCEPTION(String msg) {
        super(msg);
    }
}
