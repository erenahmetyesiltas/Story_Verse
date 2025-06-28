package com.sweproject.storyVerse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contributor")
@Getter
@Setter
public class Contributor extends User{

    @ManyToMany(mappedBy = "contributors")
    List<Story> stories;
}
