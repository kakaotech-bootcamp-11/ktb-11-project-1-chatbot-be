package org.ktb.chatbotbe.domain.schedule.repository;

import org.ktb.chatbotbe.domain.schedule.entity.CampSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CampScheduleRepository extends JpaRepository<CampSchedule, Integer> {
    @Query("SELECT schedule FROM CampSchedule schedule WHERE schedule.date BETWEEN :startDate AND :endDate ORDER BY schedule.date asc")
    List<CampSchedule> findAllByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
