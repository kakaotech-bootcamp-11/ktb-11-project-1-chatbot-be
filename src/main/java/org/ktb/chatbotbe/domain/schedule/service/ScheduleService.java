package org.ktb.chatbotbe.domain.schedule.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.schedule.dto.CampScheduleInfo;
import org.ktb.chatbotbe.domain.schedule.entity.CampSchedule;
import org.ktb.chatbotbe.domain.schedule.repository.CampScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final CampScheduleRepository scheduleRepository;

    public List<CampScheduleInfo> getMonthEvents(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<CampSchedule> allByMonth = scheduleRepository.findAllByMonth(startDate, endDate);
        Map<LocalDate, List<String>> map = allByMonth.stream()
                .collect(Collectors.groupingBy(
                        CampSchedule::getDate,
                        Collectors.mapping(CampSchedule::getName, Collectors.toList())
                ));

        return map.entrySet().stream()
                .map(entry -> CampScheduleInfo.builder()
                        .date(entry.getKey())
                        .description(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }


}
