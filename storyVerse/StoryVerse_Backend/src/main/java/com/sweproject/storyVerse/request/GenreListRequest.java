package com.sweproject.storyVerse.request;

import com.sweproject.storyVerse.enums.GenreType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GenreListRequest {
    ArrayList<String> genreNames;
}
