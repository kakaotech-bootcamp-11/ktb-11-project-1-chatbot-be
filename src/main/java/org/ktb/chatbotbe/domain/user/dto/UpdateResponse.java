package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateResponse {
    private Long id;
    private String comment;

    @Builder
    public UpdateResponse(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
