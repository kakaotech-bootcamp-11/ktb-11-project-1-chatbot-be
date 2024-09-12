package org.ktb.chatbotbe.domain.chat.dto.service.response.strategy;


import lombok.Builder;
import lombok.Getter;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageType;


@Getter
@Builder
public class ChatMessageResponse implements ChatAIResponse {
    private ChatMessageType type;
    private Long chatId;
    private String content;
}
