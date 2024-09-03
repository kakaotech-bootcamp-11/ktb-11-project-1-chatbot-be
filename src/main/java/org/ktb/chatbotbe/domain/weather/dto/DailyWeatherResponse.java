package org.ktb.chatbotbe.domain.weather.dto;

import java.util.List;

public record DailyWeatherResponse(
        PreprocessedDailyWeather now,
        List<WeatherInfoPerThreeHour> after
) {

}
