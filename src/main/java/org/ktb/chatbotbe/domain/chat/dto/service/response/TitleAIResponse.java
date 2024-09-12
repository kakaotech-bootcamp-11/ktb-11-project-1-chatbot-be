package org.ktb.chatbotbe.domain.chat.dto.service.response;


import lombok.Builder;
import lombok.Getter;

@Getter
public class TitleAIResponse implements ChatAIResponse{
    private ChatMessageType chatMessageType;
    private String title;

    @Builder
    public TitleAIResponse(ChatMessageType chatMessageType,  String title) {
        this.chatMessageType = chatMessageType;
        this.title = title;
    }
}
