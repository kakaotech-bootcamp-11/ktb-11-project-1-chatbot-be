package org.ktb.chatbotbe.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String recommendedBy;

    private String recommendation;

    private String kakaoMapUrl;

    @Embedded
    private RestaurantImages images;


    public List<String> getRestaurantImageName() {
        return List.of(
                images.getImageUrl(1),
                images.getImageUrl(2),
                images.getImageUrl(3),
                images.getImageUrl(4)
        );
    }

}
