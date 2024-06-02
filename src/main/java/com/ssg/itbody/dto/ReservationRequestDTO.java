package com.ssg.itbody.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDTO {
    private Long trainerId;
    private Long memberId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String color;

    public ReservationRequestDTO(LocalDateTime startTime, LocalDateTime endTime, String color, Long memberId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
        this.memberId = memberId;
    }
}
