package com.sweproject.storyVerse.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate = new Date();
}
