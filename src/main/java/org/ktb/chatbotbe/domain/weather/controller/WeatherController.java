package org.ktb.chatbotbe.domain.weather.controller;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.weather.service.WeatherInfoPerThreeHour;
import org.ktb.chatbotbe.domain.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping()
    public ResponseEntity<NowWeatherResponse> getTodayWeather() {
        return ResponseEntity.ok().body(weatherService.getTodayWeather());
    }

}
