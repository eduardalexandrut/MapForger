package com.example.mapforgegameplay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TurnResultDTO {
    private String type;               // "MOVE" or "ATTACK"
    private Integer actorId;
    private Integer x;                 // null if attack
    private Integer y;                 // null if attack
    private Integer targetId;          // null if move
    private Integer damage;            // null if move
    private Boolean turnEnded;
    private Integer deadActorId;      // null unless someone died
    private Boolean gameFinished;     // true if no actors remain
    private Integer remainingHp;
}