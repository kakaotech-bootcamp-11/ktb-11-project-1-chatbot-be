package org.ktb.chatbotbe.domain.weather.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WeeklyWeatherResponse(
        String day,
        LocalDate date,
        String icon,
        Double avg_temp,
        Double max_temp,
        Double min_temp,
        Long humidity,
        String description
) {

}
