package org.ktb.chatbotbe.domain.weather.service;

import java.time.LocalDateTime;

public record WeatherInfoPerThreeHour(
        Double temp,
        Double tempMin,
        Double tempMax,
        String weatherIcon,
        LocalDateTime dateTime
) {

}
