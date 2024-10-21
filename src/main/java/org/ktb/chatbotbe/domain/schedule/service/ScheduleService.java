package org.ktb.chatbotbe.domain.schedule.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.chatbotbe.domain.schedule.dto.CampScheduleInfo;
import org.ktb.chatbotbe.domain.schedule.dto.ScheduleInfo;
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

        Map<LocalDate, List<ScheduleInfo>> map = scheduleRepository.findAllByMonth(startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                        CampSchedule::getDate,
                        Collectors.mapping(schedule -> new ScheduleInfo(schedule.getName(), schedule.getTag().name()), Collectors.toList())
                ));

        return map.entrySet().stream()
                .map(entry -> CampScheduleInfo.builder()
                        .date(entry.getKey())
                        .scheduleList(entry.getValue())  // 이름과 태그가 포함된 리스트 추가
                        .build())
                .collect(Collectors.toList());
    }


}
