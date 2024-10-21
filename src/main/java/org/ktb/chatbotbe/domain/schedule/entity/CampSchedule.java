package org.ktb.chatbotbe.domain.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "camp_schedule")
public class CampSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private CampEventTag tag;
}
