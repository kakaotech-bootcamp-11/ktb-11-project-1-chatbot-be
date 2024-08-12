package org.ktb.chatbotbe.domain.weather.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {
    MockMvc mockMvc;

    @DisplayName("오늘 날씨 정보 가져오기")
    void getWeather() throws Exception {
        mockMvc.perform(get("/api/weather"))
                .andExpect(status().isOk());
    }
}