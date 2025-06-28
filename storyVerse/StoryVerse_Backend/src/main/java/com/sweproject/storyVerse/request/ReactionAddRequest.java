package com.sweproject.storyVerse.request;

import com.sweproject.storyVerse.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionAddRequest {
    private StatusType statusType;
}
