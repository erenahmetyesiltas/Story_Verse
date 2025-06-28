package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Reaction findByUserIdAndBranchId(Long userId, Long branchId);
    Reaction findByUserIdAndStoryId(Long userId, Long storyId);
}
