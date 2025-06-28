package com.sweproject.storyVerse.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryWithBranchesResponse {
    private Long id;
    private String title;
    private String description;
    private String text;
    private boolean isDraft;
    private int totalContributorsNumber;
    private double rate;
    private List<BranchDTO> branches;
}