package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private String username;
    private String profileImage;
    private String address;

    @Builder
    public UserInfo(String username, String profileImage, String address) {
        this.username = username;
        this.profileImage = profileImage;
        this.address = address;
    }
}
