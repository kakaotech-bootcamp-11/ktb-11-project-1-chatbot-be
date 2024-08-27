package org.ktb.chatbotbe.domain.user.dto;

import lombok.Getter;

@Getter
public class AddressUpdateRequest {
    private String street;
    private String city;
    private String state;
}
