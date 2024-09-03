package org.ktb.chatbotbe.domain.chat.dto.service.response;


import lombok.Builder;
import lombok.Getter;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6


@Getter
@Builder
<<<<<<< HEAD
public class ChatMessageResponse {
    private Long chatMessageId;
    private String content;
    private Boolean isUser;
=======
public class ChatMessageResponse implements ChatAIResponse{
    private ChatMessageType type;
    private Long chatId;
    private String content;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
}
