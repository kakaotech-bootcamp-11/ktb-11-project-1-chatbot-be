package org.ktb.chatbotbe.domain.weather.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Data
public class WeekWeatherResponse {
    private String cod;
    private int message;
    private int cnt;
    private List<WeekWeatherData> list;
    private Map<String, Object> city;
    private String country;
    private int population;
    private int timezone;
    private int sunrise;
    private int sunset;

    @Data
    static class WeekWeatherData {
        private Long dt;
        private Map<String, Double> main;
        private List<Map<String, Object>> weather;
        private Map<String, Object> clouds;
        private Map<String, Object> wind;
        private int visibility;
        private float pop;
        private Map<String, Object> sys;
        private String dt_txt;

        public WeatherInfoPerThreeHour toDto() {
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Double temp = main.get("temp");
            Double tempMin = main.get("temp_min");
            Double tempMax =  main.get("temp_max");
            String weatherIcon = weather.get(0).get("icon").toString();
            LocalDateTime dateTime = LocalDateTime.parse(dt_txt, DATE_TIME_FORMATTER);
            dateTime = dateTime.plusHours(9);

            return new WeatherInfoPerThreeHour(temp, tempMin, tempMax, weatherIcon, dateTime);
        }
    }
}

