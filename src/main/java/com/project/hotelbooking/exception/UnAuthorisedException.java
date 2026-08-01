package com.project.hotelbooking.exception;

public class UnAuthorisedException extends RuntimeException{
    public UnAuthorisedException(String message) {
        super(message);
    }
}
