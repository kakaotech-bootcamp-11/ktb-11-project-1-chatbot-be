package org.ktb.chatbotbe.domain.chat.dto.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewChatResponse {
<<<<<<< HEAD
    private String title;
    private Long chatId;
    private ChatMessageResponse aiResponse;
=======
    private ChatAIResponse aiResponse;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
}