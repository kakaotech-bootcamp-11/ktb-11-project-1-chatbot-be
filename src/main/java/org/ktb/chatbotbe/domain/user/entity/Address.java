package org.ktb.chatbotbe.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public class Address {
<<<<<<< HEAD
    private String street;
    private String city;
    private String state;
=======
    private String address;
    private String zipNo;
    private String addrDetail;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6

    public Address() {
    }

    @Builder
    public Address(String street, String city, String state) {
<<<<<<< HEAD
        this.street = street;
        this.city = city;
        this.state = state;
=======
        this.address = street;
        this.zipNo = city;
        this.addrDetail = state;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    }

    @Override
    public String toString() {
<<<<<<< HEAD
        return state + " " + street + " " + city + " " + street;
=======
        return address + " " + addrDetail + " " + zipNo;
>>>>>>> 5be43d597872c1111545c7c0eea914b27ceb83f6
    }
}
