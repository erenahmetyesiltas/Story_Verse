package com.sweproject.storyVerse.response;

import com.sweproject.storyVerse.entity.Branch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponse {

    private String title;
    private String text;
    private String contributorEmail;
    private Long likeCounter;
    private Long dislikeCounter;

    public BranchResponse(Branch branch){
        this.title = branch.getTitle();
        this.text = branch.getText();
        this.contributorEmail = branch.getContributor().getEmail();
        this.likeCounter = branch.getLikeCounter();
        this.dislikeCounter = branch.getDislikeCounter();
    }
}
