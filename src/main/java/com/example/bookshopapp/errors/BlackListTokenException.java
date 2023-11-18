package com.example.bookshopapp.errors;

import io.jsonwebtoken.JwtException;

public class BlackListTokenException extends JwtException {

    public BlackListTokenException(String message) {
        super(message);
    }
}
