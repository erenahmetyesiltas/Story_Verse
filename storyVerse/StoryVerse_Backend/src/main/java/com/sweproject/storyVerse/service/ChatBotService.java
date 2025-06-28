package com.sweproject.storyVerse.service;

import com.openai.models.responses.*;
import com.sweproject.storyVerse.conversation.ConversationState;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.sweproject.storyVerse.entity.Story;
import com.sweproject.storyVerse.exception.ChatBotException;
import com.sweproject.storyVerse.exception.StoryDescriptionNotFoundException;
import com.sweproject.storyVerse.repository.StoryRepository;
import com.sweproject.storyVerse.response.StoryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class ChatBotService {

    private final OpenAIClient client;
    private final ConversationState state;
    private final StoryService storyService;
    private Boolean isDescriptionAdd = false;

    @Autowired
    public ChatBotService(OpenAIClient client, StoryService storyService) {
        this.client = client;
        this.state = new ConversationState();
        this.storyService = storyService;
    }
    
    // Before use chatBot first add description of the story to the state.
    public void addStoryDescription(Long storyId) {
        try {
            String storyDescription = storyService.getStoryDescription(storyId);
            if (storyDescription == null) {
                throw new StoryDescriptionNotFoundException("Story description for ID " + storyId + " is missing.");
            }
            state.addSystemMessage(storyDescription);
        } catch (Exception e) {
            throw new ChatBotException("Failed to load story description for story ID " + storyId, e);
        }
    }

    public String sendMessage(String userMessage, Long storyId) {
        try {
            if (!isDescriptionAdd) {
                isDescriptionAdd = true;
                this.addStoryDescription(storyId);
            }

            // Add user message
            state.addUserMessage(userMessage);

            // Prepare API parameter
            ResponseCreateParams params = ResponseCreateParams.builder()
                    .model(ChatModel.GPT_4_1_NANO)
                    .inputOfResponse(state.getMessages())
                    .maxOutputTokens(256)
                    .build();

            // Get response
            List<ResponseOutputMessage> outputs = client.responses().create(params)
                    .output().stream()
                    .flatMap(o -> o.message().stream())
                    .collect(toList());

            StringBuilder response = new StringBuilder();
            for (ResponseOutputMessage message : outputs) {
                state.addAssistantMessage(message); // Add message history
                message.content().stream()
                        .flatMap(c -> c.outputText().stream())
                        .forEach(text -> response.append(text.text()).append("\n"));
            }
            return response.toString().trim();
        } catch (ChatBotException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ChatBotException("Failed to send or process chatbot response", e);
        }

    }

}
