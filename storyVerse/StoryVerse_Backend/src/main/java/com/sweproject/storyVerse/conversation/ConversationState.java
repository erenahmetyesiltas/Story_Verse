package com.sweproject.storyVerse.conversation;

import com.openai.models.responses.EasyInputMessage;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseOutputMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class ConversationState {

    private final List<ResponseInputItem> messages = new ArrayList<>();

    public ConversationState() {
        // System message, define model's role
        messages.add(ResponseInputItem.ofEasyInputMessage(EasyInputMessage.builder()
                .role(EasyInputMessage.Role.SYSTEM)
                .content("You are a helpful assistant that helps writing stories.Be creative and use maximum 256 output tokens. " +
                        "End the paragraph with a complete sentence.")
                .build()));
    }

    public List<ResponseInputItem> getMessages() {
        return messages;
    }


    public void addUserMessage(String content) {
        messages.add(ResponseInputItem.ofEasyInputMessage(EasyInputMessage.builder()
                .role(EasyInputMessage.Role.USER)
                .content(content)
                .build()));
    }

    public void addAssistantMessage(ResponseOutputMessage message) {
        messages.add(ResponseInputItem.ofResponseOutputMessage(message));
    }

    public void addSystemMessage(String message) {
        messages.add(ResponseInputItem.ofEasyInputMessage(EasyInputMessage.builder()
                .role(EasyInputMessage.Role.SYSTEM)
                .content(message)
                .build()));
    }
}