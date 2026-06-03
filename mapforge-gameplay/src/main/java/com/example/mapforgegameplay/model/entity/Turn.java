package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static Turn create(UUID campaignId, Integer index, Integer actorId) {
        Turn t = new Turn();
        t.id = new TurnPK(index, campaignId);
        t.actorId = actorId;
        t.completed = false;
        t.createdAt = LocalDateTime.now();
        return t;
    }
}