package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Author;
import com.sweproject.storyVerse.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoryIdAndBranchId(Long storyId, Long branchId);
    List<Review> findByBranchId(Long branchId);
    Review findByStoryIdAndUserId(Long storyId, Long userId);
    Review findByBranchIdAndUserId(Long branchId, Long userId);
}
