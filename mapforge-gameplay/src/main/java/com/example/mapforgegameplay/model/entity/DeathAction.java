package com.example.mapforgegameplay.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "death_actions")
public class DeathAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "actor_id", nullable = false)
    private Integer actorId;

    @Column(name = "killer_id", nullable = false)
    private Integer killerId;

    @Column(name = "turn_index", nullable = false)
    private Integer turnIndex;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}