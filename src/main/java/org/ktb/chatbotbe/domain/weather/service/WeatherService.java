package org.ktb.chatbotbe.domain.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.latitude}")
    private Double latitude;
    @Value("${weather.longitude}")
    private Double longitude;
    @Value("${weather.api-key}")
    private String WeatherAPIKey;
    private final WebClient webClient;


    public List<WeatherInfoPerThreeHour> getTodayWeather() {
        WeekWeatherResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("appid", WeatherAPIKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "kr")
                        .build()
                )
                .retrieve()
                .bodyToMono(WeekWeatherResponse.class)
                .block();

        return response.getList().stream()
                .map(WeekWeatherResponse.WeekWeatherData::toDto)
                .limit(10)
                .toList();
    }

}
