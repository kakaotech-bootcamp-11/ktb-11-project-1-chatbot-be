package org.ktb.chatbotbe.domain.user.dto;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private Long id;
    private String comment;
}
