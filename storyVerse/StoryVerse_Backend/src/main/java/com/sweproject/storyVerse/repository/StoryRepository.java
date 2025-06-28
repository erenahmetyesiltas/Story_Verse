package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Author;
import com.sweproject.storyVerse.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findStoryByAuthorIdAndIsDraft(Long id, boolean isDraft);

    Story findStoryByAuthorIdAndId(Long id, Long storyId);

    Story findStoryById(Long id);

    List<Story> findByTitleStartingWith(String prefix);
}
