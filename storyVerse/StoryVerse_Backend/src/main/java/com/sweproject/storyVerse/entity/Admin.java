package com.sweproject.storyVerse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(name = "admin")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User{

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;
}
