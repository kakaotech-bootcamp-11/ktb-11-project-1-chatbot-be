package org.ktb.chatbotbe.domain.chat.dto.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatHistory {
    private Long chatMessageId;
    private Boolean isUser;
    private String content;

    @Builder
    public ChatHistory(Long chatMessageId, Boolean isUser, String content) {
        this.chatMessageId = chatMessageId;
        this.isUser = isUser;
        this.content = content;
    }
}
