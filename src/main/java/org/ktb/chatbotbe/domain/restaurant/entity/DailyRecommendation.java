package org.ktb.chatbotbe.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class DailyRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private LocalDate date;

    @Builder
    public DailyRecommendation(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.date = LocalDate.now();
    }
}
