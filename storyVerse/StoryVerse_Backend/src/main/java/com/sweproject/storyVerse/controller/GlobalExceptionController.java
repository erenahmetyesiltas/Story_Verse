package com.sweproject.storyVerse.controller;

import com.sweproject.storyVerse.exception.AuthorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleAuthorNotFoundException(AuthorNotFoundException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.NOT_FOUND.value());
        errorBody.put("error", "Not Found");
        errorBody.put("message", ex.getMessage());
        return errorBody;
    }
}
