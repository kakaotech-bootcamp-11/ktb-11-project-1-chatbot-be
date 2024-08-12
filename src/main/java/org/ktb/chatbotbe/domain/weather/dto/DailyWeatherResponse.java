package org.ktb.chatbotbe.domain.weather.dto;

import java.util.List;
import java.util.Map;

public record DailyWeatherResponse(
        Map<String, Object> now,
        List<WeatherInfoPerThreeHour> after
) {

}
