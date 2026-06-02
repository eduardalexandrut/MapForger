package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class SessionPresence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}
