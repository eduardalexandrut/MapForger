package com.example.mapforgegameplay.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attack_actions")
public class AttackAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "attacker_id", nullable = false)
    private Integer attackerId;         // CampaignActor id from MapForge

    @Column(name = "attacked_id", nullable = false)
    private Integer attackedId;         // CampaignActor id from MapForge

    @Column(name = "turn_index", nullable = false)
    private Integer turnIndex;

    @Column(name = "campaign_id", nullable = false)
    private java.util.UUID campaignId;

    @Column(name = "damage", nullable = false)
    private Integer damage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}