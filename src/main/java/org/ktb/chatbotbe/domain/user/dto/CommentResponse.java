package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String comment;

    @Builder
    public CommentResponse(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
