package com.sweproject.storyVerse.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message){
        super(message);
    }
}
