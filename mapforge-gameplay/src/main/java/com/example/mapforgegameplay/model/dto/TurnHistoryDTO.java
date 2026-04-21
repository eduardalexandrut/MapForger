package com.example.mapforgegameplay.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TurnHistoryDTO {
    private Integer index;
    private Integer actorId;
    private Boolean completed;
    private List<ActionHistoryDTO> actions;
}