package org.ktb.chatbotbe.domain.weather.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class DayWeather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String day;
    private LocalDate date;
    private String icon;
    private Double avg_temp;
    private Double max_temp;
    private Double min_temp;
    private Long humidity;
    private String description;

    @Builder
    public DayWeather(String day, LocalDate date, String icon, Double avg_temp, Double max_temp, Double min_temp, Long humidity, String description) {
        this.day = day;
        this.date = date;
        this.icon = icon;
        this.avg_temp = avg_temp;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.humidity = humidity;
        this.description = description;
    }
}
