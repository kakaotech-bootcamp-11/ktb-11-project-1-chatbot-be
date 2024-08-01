package org.ktb.chatbotbe.domain.chat.dto.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageCreateRequest {
    private String content;
}
