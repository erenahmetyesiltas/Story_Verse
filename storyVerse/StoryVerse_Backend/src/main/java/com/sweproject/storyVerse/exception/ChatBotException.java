package com.sweproject.storyVerse.exception;

public class ChatBotException extends RuntimeException {
  public ChatBotException(String message) {
    super(message);
  }

  public ChatBotException(String message, Throwable cause) {
    super(message, cause);
  }
}