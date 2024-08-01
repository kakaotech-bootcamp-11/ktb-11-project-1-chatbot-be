package org.ktb.chatbotbe.domain.chat.dto.service.response;

import lombok.Builder;

@Builder
public record ChatResponse(
        Long id,
        String title
) {

}
