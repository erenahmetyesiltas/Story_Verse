package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    //List<Branch> findByStoryId(Long storyId);

    @Query("SELECT b FROM Branch b Where b.story.id = :storyId and b.parentBranch IS NULL")
    List<Branch> _findByStoryId(Long storyId);

    List<Branch> findByParentBranch_Id(Long parentBranchİd);

}
