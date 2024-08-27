package org.ktb.chatbotbe.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ktb.chatbotbe.global.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private Long socialId;

    @Embedded
    private Address address;

    @Builder
    public User(String nickname, Long socialId) {
        this.nickname = nickname;
        this.socialId = socialId;
    }

    public void updateAddress(String street, String city, String state) {
        this.address = Address.builder()
                .street(street)
                .city(city)
                .state(state)
                .build();
    }

    public String getAddress(){
        if (address != null) {
            return address.toString();
        }
        return null;
    }
}
