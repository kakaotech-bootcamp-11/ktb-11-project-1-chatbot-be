package org.ktb.chatbotbe.domain.weather.dto;

public record WeeklyWeatherResponse(
        String day,
        String icon,
        Double avg_temp,
        Double max_temp,
        Double min_temp,
        Double rain
) {

}
