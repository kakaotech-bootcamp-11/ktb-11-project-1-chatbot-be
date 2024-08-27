package org.ktb.chatbotbe.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String state;

    public Address() {
    }

    @Builder
    public Address(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
    }

    @Override
    public String toString() {
        return state + " " + street + " " + city + " " + street;
    }
}
