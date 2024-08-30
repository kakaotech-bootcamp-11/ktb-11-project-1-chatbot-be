package org.ktb.chatbotbe.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public class Address {
    private String address;
    private String zipNo;
    private String addrDetail;

    public Address() {
    }

    @Builder
    public Address(String street, String city, String state) {
        this.address = street;
        this.zipNo = city;
        this.addrDetail = state;
    }

    @Override
    public String toString() {
        return address + " " + addrDetail + " " + zipNo;
    }
}
