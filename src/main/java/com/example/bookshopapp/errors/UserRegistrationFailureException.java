package com.example.bookshopapp.errors;

public class UserRegistrationFailureException extends RuntimeException {
    public UserRegistrationFailureException(String message) {
        super(message);
    }
}
