package org.ktb.chatbotbe.domain.weather.respository;

import org.ktb.chatbotbe.domain.weather.Entity.DayWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DayWeatherRepository extends JpaRepository<DayWeather, Long> {
    boolean existsByDate(LocalDate date);
    Optional<DayWeather> findByDate(LocalDate date);
}
