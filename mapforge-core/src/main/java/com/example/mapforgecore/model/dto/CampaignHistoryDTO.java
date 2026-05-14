package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CampaignHistoryDTO {
    private UUID campaignId;
    private GameStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer totalTurns;
    private List<TurnHistoryDTO> turns;
}