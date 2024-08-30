package org.ktb.chatbotbe.domain.schedule.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class CampScheduleInfo implements Serializable {
    LocalDate date;
    List<String> description;
    @Builder
    public CampScheduleInfo(LocalDate date, List<String> description) {
        this.date = date;
        this.description = description;
    }
}