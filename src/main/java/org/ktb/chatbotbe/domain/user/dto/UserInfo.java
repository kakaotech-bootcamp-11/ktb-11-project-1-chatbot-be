package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private String username;
    private String profileImage;

    @Builder
    public UserInfo(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }
}
