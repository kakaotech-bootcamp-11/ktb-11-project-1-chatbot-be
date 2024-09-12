package org.ktb.chatbotbe.domain.chat.dto.service.response.strategy;

import lombok.Getter;
import org.ktb.chatbotbe.domain.chat.dto.service.response.ChatMessageType;

@Getter
public class DoneResponse implements ChatAIResponse {
    private final ChatMessageType type = ChatMessageType.DONE;
}
