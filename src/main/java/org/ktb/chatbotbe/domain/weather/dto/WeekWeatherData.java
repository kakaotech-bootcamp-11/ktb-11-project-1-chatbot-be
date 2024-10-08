package org.ktb.chatbotbe.domain.weather.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
public class WeekWeatherData {
    private Long dt;
    private Map<String, Double> main;
    private List<Map<String, Object>> weather;
    private Map<String, Object> clouds;
    private Map<String, Object> wind;
    private int visibility;
    private float pop;
    private Map<String, Double> rain;
    private Map<String, Object> sys;
    private String dt_txt;

    public WeatherInfoPerThreeHour toDto() {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Double temp = main.get("temp");
        Double tempMin = main.get("temp_min");
        Double tempMax = main.get("temp_max");
        String weatherIcon = weather.get(0).get("icon").toString();
        LocalDateTime dateTime = LocalDateTime.parse(dt_txt, DATE_TIME_FORMATTER);
        dateTime = dateTime.plusHours(9);
        Long humidity = main.get("humidity").longValue();
        String description = weather.get(0).get("description").toString();

        return WeatherInfoPerThreeHour.builder()
                .temp(temp)
                .tempMin(tempMin)
                .tempMax(tempMax)
                .humidity(humidity)
                .weatherIcon(weatherIcon)
                .dateTime(dateTime)
                .description(description)
                .build();
    }
}
