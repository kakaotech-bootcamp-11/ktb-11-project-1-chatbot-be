package org.ktb.chatbotbe.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.weather.dto.WeeklyWeatherResponse;
import org.ktb.chatbotbe.domain.weather.service.WeatherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherScheduler {
    private final WeatherService weatherService;

    @Scheduled(cron = "0 0 * * *")
    public void setupWeather(){
        List<WeeklyWeatherResponse> weekWeather = weatherService.getWeekWeather();
        for (WeeklyWeatherResponse weather : weekWeather){
            if (!weatherService.existDate(weather.date())){
                weatherService.saveDayWeather(weather);
            }
        }
    }
}
