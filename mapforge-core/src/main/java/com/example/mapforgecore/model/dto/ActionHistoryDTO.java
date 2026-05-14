package com.example.mapforgecore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActionHistoryDTO {
    private String type;        // "MOVE", "ATTACK", "DEATH"
    private Integer actorId;

    // MOVE fields
    private Integer x;
    private Integer y;

    // ATTACK fields
    private Integer targetId;
    private Integer damage;

    // DEATH fields
    private Integer killerId;

    private LocalDateTime cretedAt;
}