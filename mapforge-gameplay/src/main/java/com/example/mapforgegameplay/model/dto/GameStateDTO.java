package com.example.mapforgegameplay.model.dto;


import com.example.mapforgegameplay.model.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GameStateDTO {
    private UUID campaignId;
    private GameStatus status;
    private Integer currentTurnIndex;
    private Integer currentActorId;        // whose turn it is
    private List<Integer> turnOrder;       // ordered list of actorIds
}
