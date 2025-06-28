package com.sweproject.storyVerse.exception;

public class BranchPostException extends RuntimeException {
    public BranchPostException(String message) {
        super(message);
    }

    public BranchPostException(String message, Throwable cause) {
        super(message, cause);
    }
}