package com.sweproject.storyVerse.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sweproject.storyVerse.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class AuthorCreateRequest {
    private Gender gender;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String description;
    private LocalDate birthDate;
    private int followerCount; // computedColumn
    private int totalNumberOfStory; // computed column

}