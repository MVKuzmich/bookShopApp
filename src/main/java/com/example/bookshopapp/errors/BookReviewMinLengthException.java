package com.example.bookshopapp.errors;

public class BookReviewMinLengthException extends RuntimeException {
    public BookReviewMinLengthException(String message) {
        super(message);
    }
}
