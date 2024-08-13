package org.ktb.chatbotbe.domain.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.weather.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;


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

    public DailyWeatherResponse getTodayWeather() {
        DailyWeatherInfo nowWeather = getDailyWeatherInfo();
        WeeklyWeatherInfo response = getWeeklyWeatherInfo();
        Map<String, Object> now = convertDailyWeather(nowWeather);
        List<WeatherInfoPerThreeHour> after = convertWeeklyWeatherInfo(response, 7);

        return new DailyWeatherResponse(now, after);
    }

    public List<WeeklyWeatherResponse> getWeekWeather() {
        WeeklyWeatherInfo response = getWeeklyWeatherInfo();
        List<WeatherInfoPerThreeHour> weatherData = convertWeeklyWeatherInfo(response);

        Map<String, List<WeatherInfoPerThreeHour>> map = new LinkedHashMap<>();
        for (WeatherInfoPerThreeHour weatherInfoPerThreeHour : weatherData) {
            LocalDateTime dateTime = weatherInfoPerThreeHour.dateTime();
            String key = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(weatherInfoPerThreeHour);
        }

        String nowDayOfWeek = weatherData.get(0).dateTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        map.remove(nowDayOfWeek);
        List<WeeklyWeatherResponse> result = new ArrayList<>();

        map.forEach((key, value) -> {
            String icon;
            double dayMinTemp = Double.MAX_VALUE;
            double dayMaxTemp = -Double.MAX_VALUE;
            Double avgTemp = (double) 0;
            Double avgRain = (double) 0;

            for (WeatherInfoPerThreeHour data : value) {
                dayMinTemp = Double.min(dayMinTemp, data.tempMin());
                dayMaxTemp = Double.max(dayMaxTemp, data.tempMax());
                avgTemp += data.temp();
                avgRain += data.rian();
            }

            avgTemp = avgTemp / value.size();
            avgTemp = Math.round(avgTemp * 100.00) / 100.00;
            avgRain = avgRain / value.size();
            avgRain = Math.round(avgRain * 100.00) / 100.00;
            if (value.get(2) != null) {
                 icon = value.get(2).weatherIcon();
            } else {
                icon = value.get(0).weatherIcon();
            }

            result.add(new WeeklyWeatherResponse(key, icon, avgTemp, dayMaxTemp, dayMinTemp, avgRain));
        });

        return result;
    }

    private DailyWeatherInfo getDailyWeatherInfo() {
        return webClient.get()
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
                .bodyToMono(DailyWeatherInfo.class)
                .block();
    }

    private WeeklyWeatherInfo getWeeklyWeatherInfo() {
        return webClient.get()
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
                .bodyToMono(WeeklyWeatherInfo.class)
                .block();
    }

    private Map<String, Object> convertDailyWeather(DailyWeatherInfo nowWeather) {
        Map<String, Object> now = new HashMap<>();
        now.put("temp", nowWeather.getMain().get("temp"));
        now.put("temp_min", nowWeather.getMain().get("temp_min"));
        now.put("temp_max", nowWeather.getMain().get("temp_max"));
        now.put("description", nowWeather.getWeather().get(0).get("description"));
        now.put("icon", nowWeather.getWeather().get(0).get("icon"));
        return now;
    }

    private List<WeatherInfoPerThreeHour> convertWeeklyWeatherInfo(WeeklyWeatherInfo response) {
        return response.getList().stream()
                .map(WeekWeatherData::toDto)
                .toList();
    }

    private List<WeatherInfoPerThreeHour> convertWeeklyWeatherInfo(WeeklyWeatherInfo response, int i) {
        return response.getList().stream()
                .map(WeekWeatherData::toDto)
                .limit(i)
                .toList();
    }

}
