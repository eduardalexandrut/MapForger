package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "turns")
public class Turn {

    @EmbeddedId
    private TurnPK id;

    // Plain ID — CampaignActor lives in MapForge
    @Column(name = "actor_id", nullable = false)
    private Integer actorId;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    public static Turn create(UUID campaignId, Integer index, Integer actorId) {
        Turn t = new Turn();
        t.id = new TurnPK(index, campaignId);
        t.actorId = actorId;
        t.completed = false;
        return t;
    }
}