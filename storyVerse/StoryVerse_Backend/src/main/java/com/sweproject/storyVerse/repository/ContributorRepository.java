package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Author;
import com.sweproject.storyVerse.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
}
