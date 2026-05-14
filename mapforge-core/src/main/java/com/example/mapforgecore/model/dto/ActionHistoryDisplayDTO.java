package com.example.mapforgecore.model.dto;

import java.time.LocalDateTime;

public record ActionHistoryDisplayDTO(
        String type,
        String actorName,
        String targetName,
        Integer x,
        Integer y,
        Integer damage,
        String killerName,
        LocalDateTime createdAt
) {}
