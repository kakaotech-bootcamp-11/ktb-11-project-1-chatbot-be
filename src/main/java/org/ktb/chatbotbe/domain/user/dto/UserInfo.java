package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private String username;

    @Builder
    public UserInfo(String username) {
        this.username = username;
    }
}
