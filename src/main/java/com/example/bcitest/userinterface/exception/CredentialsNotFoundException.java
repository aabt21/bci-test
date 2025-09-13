package com.example.bcitest.userinterface.exception;

import org.springframework.security.core.AuthenticationException;

public class CredentialsNotFoundException extends AuthenticationException
        {
    public CredentialsNotFoundException(String message) {
        super(message);
    }
}
