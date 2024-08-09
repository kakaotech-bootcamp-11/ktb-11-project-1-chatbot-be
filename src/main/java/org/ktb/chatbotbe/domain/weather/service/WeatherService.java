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

    public NowWeatherResponse getTodayWeather() {

        WeatherResponse nowWeather = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("data/2.5/weather")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("appid", WeatherAPIKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "kr")
                        .build()
                )
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();

        Map<String, Object> now = new HashMap<>();
        now.put("temp", nowWeather.getMain().get("temp"));
        now.put("temp_min", nowWeather.getMain().get("temp_min"));
        now.put("temp_max", nowWeather.getMain().get("temp_max"));
        now.put("description", nowWeather.getWeather().get(0).get("description"));
        now.put("icon", nowWeather.getWeather().get(0).get("icon"));



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


        List<WeatherInfoPerThreeHour> after = response.getList().stream()
                .map(WeekWeatherResponse.WeekWeatherData::toDto)
                .limit(7)
                .toList();


        return new NowWeatherResponse(now, after);
    }

}
