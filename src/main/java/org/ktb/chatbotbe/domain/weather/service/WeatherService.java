package org.ktb.chatbotbe.domain.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.weather.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
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
    private final WebClient weatherWebClient;

    public DailyWeatherResponse getTodayWeather() {
        DailyWeatherInfo nowWeather = getDailyWeatherInfo();
        WeeklyWeatherInfo response = getWeeklyWeatherInfo();
        PreprocessedDailyWeather now = convertDailyWeather(nowWeather);
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
            String description;
            double dayMinTemp = Double.MAX_VALUE;
            double dayMaxTemp = -Double.MAX_VALUE;
            Double avgTemp = (double) 0;
            Long avgHumidity = (long) 0;
            LocalDate date = value.get(0).dateTime().toLocalDate();

            for (WeatherInfoPerThreeHour data : value) {
                dayMinTemp = Double.min(dayMinTemp, data.tempMin());
                dayMaxTemp = Double.max(dayMaxTemp, data.tempMax());
                avgTemp += data.temp();
                avgHumidity += data.humidity();
            }

            avgTemp = avgTemp / value.size();
            avgTemp = Math.round(avgTemp * 100.00) / 100.00;
            avgHumidity = avgHumidity / value.size();

            if (value.get(2) != null) {
                icon = value.get(2).weatherIcon();
                description = value.get(2).description();
            } else {
                icon = value.get(0).weatherIcon();
                description = value.get(0).description();
            }

            result.add(WeeklyWeatherResponse.builder()
                    .day(key)
                    .date(date)
                    .icon(icon)
                    .avg_temp(avgTemp)
                    .max_temp(dayMaxTemp)
                    .min_temp(dayMinTemp)
                    .humidity(avgHumidity)
                    .description(description)
                    .build());
        });

        return result;
    }

    private DailyWeatherInfo getDailyWeatherInfo() {
        return weatherWebClient.get()
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
        return weatherWebClient.get()
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

    private PreprocessedDailyWeather convertDailyWeather(DailyWeatherInfo nowWeather) {
        return PreprocessedDailyWeather.builder()
                .temp((Double) nowWeather.getMain().get("temp"))
                .tempMin((Double) nowWeather.getMain().get("temp_min"))
                .tempMax((Double) nowWeather.getMain().get("temp_max"))
                .description((String) nowWeather.getWeather().get(0).get("description"))
                .icon((String) nowWeather.getWeather().get(0).get("icon"))
                .build();

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
