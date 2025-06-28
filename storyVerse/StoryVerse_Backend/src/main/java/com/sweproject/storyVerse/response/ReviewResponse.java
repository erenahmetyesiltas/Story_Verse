package com.sweproject.storyVerse.response;

import com.sweproject.storyVerse.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {

    private String title;
    private String text;
    private byte point;
    private String userEmail;

    public ReviewResponse(Review review){
        this.title = review.getTitle();
        this.text = review.getText();
        this.point = review.getPoint();
        this.userEmail = review.getUser().getEmail();
    }
}
