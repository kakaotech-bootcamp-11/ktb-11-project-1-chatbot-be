package org.ktb.chatbotbe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${api-uri.weather}")
    private String weatherAPIURI;
    @Value("${api-uri.ai-server}")
    private String chatServerURI;

    @Bean
    public WebClient weatherWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(weatherAPIURI)
                .build();
    }

    @Bean
    public WebClient chatWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(chatServerURI)              .build();
    }
}
