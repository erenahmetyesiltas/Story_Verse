package com.sweproject.storyVerse.entity;

import com.sweproject.storyVerse.enums.GenreType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "genre")
@Entity
public class Genre extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private GenreType name;

    @ManyToMany(mappedBy = "genres")
    private List<Story> stories;

    @ManyToMany(mappedBy = "genres")
    private List<User> users;
}