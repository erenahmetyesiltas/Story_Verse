package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Author;
import com.sweproject.storyVerse.entity.Genre;
import com.sweproject.storyVerse.enums.GenreType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(GenreType name);
}
