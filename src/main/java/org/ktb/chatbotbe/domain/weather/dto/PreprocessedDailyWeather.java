package org.ktb.chatbotbe.domain.weather.dto;

import lombok.Builder;

@Builder
public record PreprocessedDailyWeather(
        Double temp,
        Double tempMin,
        Double tempMax,
        String description,
        String icon
) {
}
