package com.sweproject.storyVerse.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private Long id;
    private String text;
    private double rate;
    private Long parentBranchId;
    private Long likeCounter;
    private Long DislikeCounter;
}
