package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommentRequest {
    private Long id;
    private String comment;
}
