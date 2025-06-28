package com.sweproject.storyVerse.response;

import com.sweproject.storyVerse.entity.Genre;
import com.sweproject.storyVerse.entity.Story;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StoryResponse {

    private String title;
    private String description;
    private String text;
    private List<String> genres;
    private String authorNameSurname;
    private Long likeCounter;
    private Long dislikeCounter;

    public StoryResponse(Story story){
        this.text = story.getText();
        this.description = story.getDescription();
        this.title = story.getTitle();
        this.authorNameSurname = story.getAuthor().getFirstName() + " " + story.getAuthor().getLastName();

        // get genres list
        List<String> genresList = new ArrayList<>();

        for (int i = 0; i < story.getGenres().size(); i++) {
            genresList.add(String.valueOf(story.getGenres().get(i).getName()));
        }

        this.genres = genresList;
        this.likeCounter = story.getLikeCounter();
        this.dislikeCounter = story.getDislikeCounter();
    }

}
