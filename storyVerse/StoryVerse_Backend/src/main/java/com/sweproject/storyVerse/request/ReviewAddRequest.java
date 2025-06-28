package com.sweproject.storyVerse.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewAddRequest {
    private String title;
    private String text;
    private byte point;
}
