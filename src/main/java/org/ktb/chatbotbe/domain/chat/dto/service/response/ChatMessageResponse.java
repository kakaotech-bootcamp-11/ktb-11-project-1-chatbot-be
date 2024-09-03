package org.ktb.chatbotbe.domain.chat.dto.service.response;


import lombok.Builder;
import lombok.Getter;



@Getter
@Builder
public class ChatMessageResponse implements ChatAIResponse{
    private ChatMessageType type;
    private Long chatId;
    private String content;
}
