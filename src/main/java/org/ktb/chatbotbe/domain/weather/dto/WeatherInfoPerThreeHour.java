package org.ktb.chatbotbe.domain.weather.dto;

import java.time.LocalDateTime;

public record WeatherInfoPerThreeHour(
        Double temp,
        Double tempMin,
        Double tempMax,
        Double rian,
        String weatherIcon,
        LocalDateTime dateTime
) {

}
