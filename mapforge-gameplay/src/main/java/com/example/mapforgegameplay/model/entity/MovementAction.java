package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "movement_actions")
public class MovementAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "actor_id", nullable = false)
    private Integer actorId;            // CampaignActor id from MapForge

    @Column(name = "turn_index", nullable = false)
    private Integer turnIndex;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;
}
