package com.sweproject.storyVerse.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatBotResponse {
    private String response;

    public ChatBotResponse(String response) {
        this.response = response;
    }

}
