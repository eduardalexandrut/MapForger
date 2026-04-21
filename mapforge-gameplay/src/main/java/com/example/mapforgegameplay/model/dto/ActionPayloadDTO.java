package com.example.mapforgegameplay.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionPayloadDTO {
    private Integer actorId;           // who is acting (must match current turn)
    private String type;               // "MOVE" or "ATTACK"

    // movement fields
    private Integer x;
    private Integer y;

    // attack fields
    private Integer targetId;
}