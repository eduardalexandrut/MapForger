package com.example.mapforgecore.model.dto;

import java.util.List;

public record TurnHistoryDisplayDTO(
        Integer index,
        String actorName,
        Boolean completed,
        List<ActionHistoryDisplayDTO> actions
) {}