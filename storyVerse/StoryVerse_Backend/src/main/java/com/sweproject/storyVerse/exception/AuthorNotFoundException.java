package com.sweproject.storyVerse.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id) {
        super("Author with id: " + id + " not found");
    }
}
