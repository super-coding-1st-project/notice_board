package com.github.supercodingspring.supercodingproject1st.service.exception;

public class InvalidValueException extends RuntimeException{
    public InvalidValueException(String message) {
        super(message);
    }
}
