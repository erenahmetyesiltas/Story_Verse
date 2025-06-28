package com.sweproject.storyVerse.exception;

public class GenrePostException extends RuntimeException {
    public GenrePostException(String message) {
        super(message);
    }

    public GenrePostException(String message, Throwable cause) {
        super(message, cause);
    }
}