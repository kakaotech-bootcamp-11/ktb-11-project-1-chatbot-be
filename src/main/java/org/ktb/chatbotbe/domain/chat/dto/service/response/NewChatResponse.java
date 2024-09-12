package org.ktb.chatbotbe.domain.chat.dto.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewChatResponse {
    private ChatAIResponse aiResponse;
}