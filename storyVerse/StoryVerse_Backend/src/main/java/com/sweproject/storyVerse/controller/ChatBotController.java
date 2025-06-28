package com.sweproject.storyVerse.controller;

import com.openai.models.responses.Response;
import com.sweproject.storyVerse.service.ChatBotService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@AllArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestParam(value = "storyId", required = true) int storyId,
                                       @RequestBody String message) {
        String response = chatBotService.sendMessage(message, (long) storyId);
        return ResponseEntity.ok(response);
    }

}
