package com.sweproject.storyVerse.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(String message){
        super(message);
    }
}
