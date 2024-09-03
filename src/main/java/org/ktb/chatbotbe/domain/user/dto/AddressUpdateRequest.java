package org.ktb.chatbotbe.domain.user.dto;

import lombok.Getter;

@Getter
public class AddressUpdateRequest {
    private String address;
    private String zipNo;
    private String addrDetail;
}
