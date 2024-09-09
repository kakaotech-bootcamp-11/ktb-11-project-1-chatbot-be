package org.ktb.chatbotbe.domain.chat.dto.service.response;

import lombok.Getter;

@Getter
public class DoneResponse implements ChatAIResponse{
    private final ChatMessageType type = ChatMessageType.DONE;
}
