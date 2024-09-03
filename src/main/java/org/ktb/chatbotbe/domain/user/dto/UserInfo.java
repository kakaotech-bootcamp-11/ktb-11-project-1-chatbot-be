package org.ktb.chatbotbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private String username;
    private String profileImage;
<<<<<<< HEAD

    @Builder
    public UserInfo(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
=======
    private String address;

    @Builder
    public UserInfo(String username, String profileImage, String address) {
        this.username = username;
        this.profileImage = profileImage;
        this.address = address;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    }
}
