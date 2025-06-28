package com.sweproject.storyVerse.request;

import com.sweproject.storyVerse.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ContributorCreateRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String description; // A description in his/her personal page
    private Gender gender;
    private LocalDate birthDate;
}
