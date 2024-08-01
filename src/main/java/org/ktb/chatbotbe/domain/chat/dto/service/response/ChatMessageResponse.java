package org.ktb.chatbotbe.domain.chat.dto.service.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class ChatMessageResponse {
    private Long chatMessageId;
    private String content;
    private Boolean isUser;
}
