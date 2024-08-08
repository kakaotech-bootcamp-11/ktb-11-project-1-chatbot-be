package org.ktb.chatbotbe.domain.weather.service;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeatherResponse {
    private Map<String, String> coord;
    private List<Map<String, Object>> weather;
    private String base;
    private Map<String, Object> main;
    private Map<String, Object> wind;
    private Long visibility;
    private Map<String, Object> clouds;
    private Long dt;
    private Map<String, Object> sys;
    private Long timezone;
    private Long id;
    private String name;
    private String cod;

}

