package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
