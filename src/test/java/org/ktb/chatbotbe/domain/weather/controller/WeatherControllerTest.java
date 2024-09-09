//package org.ktb.chatbotbe.domain.weather.controller;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.ktb.chatbotbe.domain.weather.dto.*;
//import org.ktb.chatbotbe.domain.weather.service.WeatherService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(WeatherController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class WeatherControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//    @MockBean
//    WeatherService weatherService;
//    @DisplayName("오늘 날씨 정보 가져오기 태스트")
//    @Test
//    void getWeather() throws Exception {
//        // given
//        PreprocessedDailyWeather now = PreprocessedDailyWeather.builder()
//                .temp(33.2)
//                .tempMin(28.1)
//                .tempMax(34.0)
//                .description("구름 조금")
//                .icon("03d")
//                .build();
//        List<WeatherInfoPerThreeHour> after = List.of(
//                WeatherInfoPerThreeHour.builder()
//                        .temp(31.64)
//                        .tempMin(29.49)
//                        .tempMax(31.64)
//                        .humidity(72L)
//                        .weatherIcon("10d")
//                        .dateTime(LocalDateTime.parse("2024-08-22T12:00:00"))
//                        .description("실 비")
//                        .build(),
//                WeatherInfoPerThreeHour.builder()
//                        .temp(30.57)
//                        .tempMin(29.5)
//                        .tempMax(30.57)
//                        .humidity(81L)
//                        .weatherIcon("10d")
//                        .dateTime(LocalDateTime.parse("2024-08-22T15:00:00"))
//                        .description("실 비")
//                        .build(),
//                WeatherInfoPerThreeHour.builder()
//                        .temp(27.45)
//                        .tempMin(27.45)
//                        .tempMax(27.45)
//                        .humidity(93L)
//                        .weatherIcon("10d")
//                        .dateTime(LocalDateTime.parse("2024-08-22T18:00:00"))
//                        .description("실 비")
//                        .build(),
//                WeatherInfoPerThreeHour.builder()
//                        .temp(24.65)
//                        .tempMin(24.65)
//                        .tempMax(24.65)
//                        .humidity(93L)
//                        .weatherIcon("10n")
//                        .dateTime(LocalDateTime.parse("2024-08-22T21:00:00"))
//                        .description("실 비")
//                        .build(),
//                WeatherInfoPerThreeHour.builder()
//                        .temp(24.04)
//                        .tempMin(24.04)
//                        .tempMax(24.04)
//                        .humidity(95L)
//                        .weatherIcon("03n")
//                        .dateTime(LocalDateTime.parse("2024-08-23T00:00:00"))
//                        .description("구름조금")
//                        .build(),
//                WeatherInfoPerThreeHour.builder()
//                        .temp(23.6)
//                        .tempMin(23.6)
//                        .tempMax(23.6)
//                        .humidity(97L)
//                        .weatherIcon("03n")
//                        .dateTime(LocalDateTime.parse("2024-08-23T03:00:00"))
//                        .description("구름조금")
//                        .build(),
//                WeatherInfoPerThreeHour.builder()
//                        .temp(23.21)
//                        .tempMin(23.21)
//                        .tempMax(23.21)
//                        .humidity(97L)
//                        .weatherIcon("10d")
//                        .dateTime(LocalDateTime.parse("2024-08-23T06:00:00"))
//                        .description("실 비")
//                        .build()
//        );
//
//        DailyWeatherResponse response = new DailyWeatherResponse(now, after);
//
//        when(weatherService.getTodayWeather()).thenReturn(response);
//
//        mockMvc.perform(get("/api/weather"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.now.temp").value(33.2))
//                .andExpect(jsonPath("$.now.tempMin").value(28.1))
//                .andExpect(jsonPath("$.now.tempMax").value(34.0))
//                .andExpect(jsonPath("$.now.description").value("구름 조금"))
//                .andExpect(jsonPath("$.now.icon").value("03d"))
//                .andExpect(jsonPath("$.after[0].temp").value(31.64))
//                .andExpect(jsonPath("$.after[0].tempMin").value(29.49))
//                .andExpect(jsonPath("$.after[0].tempMax").value(31.64))
//                .andExpect(jsonPath("$.after[0].humidity").value(72))
//                .andExpect(jsonPath("$.after[0].weatherIcon").value("10d"))
//                .andExpect(jsonPath("$.after[0].dateTime").value("2024-08-22T12:00:00"))
//                .andExpect(jsonPath("$.after[0].description").value("실 비"))
//                .andExpect(jsonPath("$.after[1].temp").value(30.57))
//                .andExpect(jsonPath("$.after[1].tempMin").value(29.5))
//                .andExpect(jsonPath("$.after[1].tempMax").value(30.57))
//                .andExpect(jsonPath("$.after[1].humidity").value(81))
//                .andExpect(jsonPath("$.after[1].weatherIcon").value("10d"))
//                .andExpect(jsonPath("$.after[1].dateTime").value("2024-08-22T15:00:00"))
//                .andExpect(jsonPath("$.after[1].description").value("실 비"))
//                .andExpect(jsonPath("$.after[2].temp").value(27.45))
//                .andExpect(jsonPath("$.after[2].tempMin").value(27.45))
//                .andExpect(jsonPath("$.after[2].tempMax").value(27.45))
//                .andExpect(jsonPath("$.after[2].humidity").value(93))
//                .andExpect(jsonPath("$.after[2].weatherIcon").value("10d"))
//                .andExpect(jsonPath("$.after[2].dateTime").value("2024-08-22T18:00:00"))
//                .andExpect(jsonPath("$.after[2].description").value("실 비"))
//                .andExpect(jsonPath("$.after[3].temp").value(24.65))
//                .andExpect(jsonPath("$.after[3].tempMin").value(24.65))
//                .andExpect(jsonPath("$.after[3].tempMax").value(24.65))
//                .andExpect(jsonPath("$.after[3].humidity").value(93))
//                .andExpect(jsonPath("$.after[3].weatherIcon").value("10n"))
//                .andExpect(jsonPath("$.after[3].dateTime").value("2024-08-22T21:00:00"))
//                .andExpect(jsonPath("$.after[3].description").value("실 비"))
//                .andExpect(jsonPath("$.after[4].temp").value(24.04))
//                .andExpect(jsonPath("$.after[4].tempMin").value(24.04))
//                .andExpect(jsonPath("$.after[4].tempMax").value(24.04))
//                .andExpect(jsonPath("$.after[4].humidity").value(95))
//                .andExpect(jsonPath("$.after[4].weatherIcon").value("03n"))
//                .andExpect(jsonPath("$.after[4].dateTime").value("2024-08-23T00:00:00"))
//                .andExpect(jsonPath("$.after[4].description").value("구름조금"))
//                .andExpect(jsonPath("$.after[5].temp").value(23.6))
//                .andExpect(jsonPath("$.after[5].tempMin").value(23.6))
//                .andExpect(jsonPath("$.after[5].tempMax").value(23.6))
//                .andExpect(jsonPath("$.after[5].humidity").value(97))
//                .andExpect(jsonPath("$.after[5].weatherIcon").value("03n"))
//                .andExpect(jsonPath("$.after[5].dateTime").value("2024-08-23T03:00:00"))
//                .andExpect(jsonPath("$.after[5].description").value("구름조금"))
//                .andExpect(jsonPath("$.after[6].temp").value(23.21))
//                .andExpect(jsonPath("$.after[6].tempMin").value(23.21))
//                .andExpect(jsonPath("$.after[6].tempMax").value(23.21))
//                .andExpect(jsonPath("$.after[6].humidity").value(97))
//                .andExpect(jsonPath("$.after[6].weatherIcon").value("10d"))
//                .andExpect(jsonPath("$.after[6].dateTime").value("2024-08-23T06:00:00"))
//                .andExpect(jsonPath("$.after[6].description").value("실 비"))
//                .andDo(print());
//    }
//
//    @DisplayName("이번주 날씨 가져오기 테스트")
//    @Test
//    void 이번주_날씨_가져오기() throws Exception {
//        List<WeeklyWeatherResponse> responses = List.of(
//                WeeklyWeatherResponse.builder()
//                        .day("목요일")
//                        .date(LocalDate.parse("2024-08-22"))
//                        .icon("10d")
//                        .avg_temp(23.21)
//                        .max_temp(23.21)
//                        .min_temp(23.21)
//                        .humidity(97L)
//                        .description("구름 조금")
//                        .build()
//
//        );
//        when(weatherService.getWeekWeather()).thenReturn(responses);
//
//        mockMvc.perform(get("/api/weather/week"))
//                .andExpect(status().isOk())  // HTTP 상태 코드가 200인지 확인
//                .andExpect(jsonPath("$[0].day").value("목요일"))
//                .andExpect(jsonPath("$[0].date").value("2024-08-22"))
//                .andExpect(jsonPath("$[0].icon").value("10d"))
//                .andExpect(jsonPath("$[0].avg_temp").value(23.21))
//                .andExpect(jsonPath("$[0].max_temp").value(23.21))
//                .andExpect(jsonPath("$[0].min_temp").value(23.21))
//                .andExpect(jsonPath("$[0].humidity").value(97L))
//                .andExpect(jsonPath("$[0].description").value("구름 조금"));
//    }
//}