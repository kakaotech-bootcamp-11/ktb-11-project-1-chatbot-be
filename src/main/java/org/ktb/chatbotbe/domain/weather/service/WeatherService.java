package org.ktb.chatbotbe.domain.weather.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.weather.Entity.DayWeather;
import org.ktb.chatbotbe.domain.weather.dto.*;
import org.ktb.chatbotbe.domain.weather.respository.DayWeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final DayWeatherRepository dayWeatherRepository;
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
        // 내일부터 5일 (오늘: 월) , 데이터 : [화, 수, 목, 금, 토]
        LocalDate today = LocalDate.now();

        // 내일부터 5일간의 날짜 가져오기
        List<LocalDate> dates = IntStream.rangeClosed(1, 5) // 1부터 5까지 범위 생성
                .mapToObj(today::plusDays) // 오늘 날짜로부터 각 날짜 더하기
                .collect(Collectors.toList());

        List<WeeklyWeatherResponse> dayWeatherInfo = new ArrayList<>();
        for (LocalDate date : dates) {
            if (!dayWeatherRepository.existsByDate(date)) {
                log.info("No day weather found");
                dayWeatherInfo = generateWeekWeatherInfo();
                dayWeatherInfo.forEach(info -> {
                            if (!dayWeatherRepository.existsByDate(info.date())) {
                                saveDayWeather(info);
                            }
                        }
                );
                break;
            }
        }

        if (dayWeatherInfo.isEmpty()) {
            for (LocalDate date : dates) {
                DayWeather byDate = dayWeatherRepository.findByDate(date).get();
                WeeklyWeatherResponse info = WeeklyWeatherResponse.builder()
                        .day(byDate.getDay())
                        .date(byDate.getDate())
                        .icon(byDate.getIcon())
                        .avg_temp(byDate.getAvg_temp())
                        .max_temp(byDate.getMax_temp())
                        .min_temp(byDate.getMin_temp())
                        .humidity(byDate.getHumidity())
                        .description(byDate.getDescription())
                        .build();

                dayWeatherInfo.add(info);
            }
        }
        return dayWeatherInfo;
    }


    //
//    public List<WeeklyWeatherResponse> getWeekWeather() {
    public List<WeeklyWeatherResponse> generateWeekWeatherInfo() {
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

    public boolean existDate(LocalDate date) {
        return dayWeatherRepository.existsByDate(date);
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

    @Transactional
    public void saveDayWeather(WeeklyWeatherResponse weather) {
        DayWeather dayWeather = DayWeather.builder()
                .day(weather.day())
                .date(weather.date())
                .icon(weather.icon())
                .avg_temp(weather.avg_temp())
                .max_temp(weather.max_temp())
                .min_temp(weather.min_temp())
                .humidity(weather.humidity())
                .description(weather.description())
                .build();

        dayWeatherRepository.save(dayWeather);
    }
}
