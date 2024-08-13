package org.ktb.chatbotbe.domain.weather.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WeatherInfoPerThreeHour(
        Double temp,
        Double tempMin,
        Double tempMax,
        Long humidity,
        String weatherIcon,
        LocalDateTime dateTime,
        String description
) {

}
