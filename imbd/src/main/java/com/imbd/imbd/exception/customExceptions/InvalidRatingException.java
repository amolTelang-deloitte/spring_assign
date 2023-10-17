package com.imbd.imbd.exception.customExceptions;

public class InvalidRatingException extends RuntimeException{
    public InvalidRatingException(String message) {
        super(message);
    }
}
