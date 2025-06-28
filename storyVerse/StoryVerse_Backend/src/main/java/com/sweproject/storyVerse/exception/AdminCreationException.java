package com.sweproject.storyVerse.exception;

public class AdminCreationException extends RuntimeException {

    public AdminCreationException(String message) {
        super(message);
    }

    public AdminCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
