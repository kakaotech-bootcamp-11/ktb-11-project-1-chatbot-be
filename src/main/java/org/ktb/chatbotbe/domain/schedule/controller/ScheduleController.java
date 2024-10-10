package org.ktb.chatbotbe.domain.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.schedule.dto.CampScheduleInfo;
import org.ktb.chatbotbe.domain.schedule.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService calendarService;

    @GetMapping
    public ResponseEntity<List<CampScheduleInfo>> getMonthEvents(
            @RequestParam(name = "day", required = false)
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth day
    ) {
        if (day == null) {
            day = YearMonth.now();
        }

        return ResponseEntity.ok(calendarService.getMonthEvents(day));
    }
}

