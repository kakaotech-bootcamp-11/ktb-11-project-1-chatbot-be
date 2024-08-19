package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateResponse {
    private String comment;

    @Builder
    public UpdateResponse(String comment) {
        this.comment = comment;
    }
}
