package org.ktb.chatbotbe.domain.restaurant.entity;


import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Optional;

@Embeddable
public class RestaurantImages {
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;

    public String getImageUrl(int i) {
        return switch (i) {
            case 1 -> Optional.ofNullable(this.imageUrl1).orElse("");
            case 2 -> Optional.ofNullable(this.imageUrl2).orElse("");
            case 3 -> Optional.ofNullable(this.imageUrl3).orElse("");
            case 4 -> Optional.ofNullable(this.imageUrl4).orElse("");
            default -> "";
        };
    }

}
