package com.sweproject.storyVerse.request;

import com.sweproject.storyVerse.entity.Genre;
import com.sweproject.storyVerse.enums.GenreType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoryCreateRequest {
    private String title;
    private String description;
    private String text;
    private List<Genre> genres;
}
