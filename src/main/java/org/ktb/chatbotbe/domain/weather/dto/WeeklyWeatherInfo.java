package org.ktb.chatbotbe.domain.weather.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeeklyWeatherInfo {
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


}

