package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "game_sessions")
public class GameSession {

    @Id
    @Column(name = "campaign_id")
    private UUID campaignId;          // same ID as in MapForge campaigns table

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GameStatus status;

    @Column(name = "current_turn_index", nullable = false)
    private Integer currentTurnIndex = 0;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "game_session_turn_order", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "actor_id")
    @OrderColumn(name = "position")
    private List<Integer> turnOrder = new ArrayList<>();

    public static GameSession create(UUID campaignId) {
        GameSession gs = new GameSession();
        gs.campaignId = campaignId;
        gs.status = GameStatus.WAITING;
        gs.currentTurnIndex = 0;
        return gs;
    }

    public void start() {
        this.status = GameStatus.ACTIVE;
        this.startedAt = LocalDateTime.now();
    }

    public void finish() {
        this.status = GameStatus.FINISHED;
        this.finishedAt = LocalDateTime.now();
    }
}