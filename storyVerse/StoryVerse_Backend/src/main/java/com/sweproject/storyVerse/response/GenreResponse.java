package com.sweproject.storyVerse.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreResponse {
    private String name;

    public GenreResponse(String name){
        this.name = name;
    }
}
