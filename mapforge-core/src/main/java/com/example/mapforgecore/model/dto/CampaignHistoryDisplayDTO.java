package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.GameStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CampaignHistoryDisplayDTO(
        UUID campaignId,
        GameStatus status,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        Integer totalTurns,
        List<TurnHistoryDisplayDTO> turns
) {}