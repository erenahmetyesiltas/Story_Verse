package com.sweproject.storyVerse.security.auth;

import com.sweproject.storyVerse.entity.Genre;
import com.sweproject.storyVerse.enums.Gender;
import com.sweproject.storyVerse.security.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String description;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;
}
