package org.ktb.chatbotbe.domain.restaurant.respository;

import org.ktb.chatbotbe.domain.restaurant.entity.DailyRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyRecommendationRepository extends JpaRepository<DailyRecommendation, Long> {
    List<DailyRecommendation> findByDate(LocalDate date);
}
