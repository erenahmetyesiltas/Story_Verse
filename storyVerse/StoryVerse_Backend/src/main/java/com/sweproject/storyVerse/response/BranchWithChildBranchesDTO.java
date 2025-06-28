package com.sweproject.storyVerse.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchWithChildBranchesDTO {
    private Long id;
    private String text;
    private double rate;
    private Long parentBranchId;
    private Long likeCounter;
    private Long DislikeCounter;
    private List<BranchDTO> childBranches;
}
