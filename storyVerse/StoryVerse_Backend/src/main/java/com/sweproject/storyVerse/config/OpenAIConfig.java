package com.sweproject.storyVerse.config;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey) // or use from config
                .build();
    }
}
