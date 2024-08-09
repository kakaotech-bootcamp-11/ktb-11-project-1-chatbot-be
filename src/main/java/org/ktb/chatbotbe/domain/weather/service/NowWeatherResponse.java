package org.ktb.chatbotbe.domain.weather.service;

import java.util.List;
import java.util.Map;

public record NowWeatherResponse(
        Map<String, Object> now,
        List<WeatherInfoPerThreeHour> after
) {
}
