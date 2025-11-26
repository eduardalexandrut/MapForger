package com.example.mapforgecore.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "turns")
public class Turn {
    @EmbeddedId
    private TurnPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("campaignId") // maps the campaignId in PK to this relation
    @JoinColumn(name = "campaign", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor", nullable = false)
    private CampaignActor actor;
}