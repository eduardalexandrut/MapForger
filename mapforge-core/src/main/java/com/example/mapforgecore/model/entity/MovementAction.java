package com.example.mapforgecore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "movement_actions")
public class MovementAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor", nullable = false)
    private CampaignActor actor;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "turn_index", referencedColumnName = "index", nullable = false),
            @JoinColumn(name = "campaign_id", referencedColumnName = "campaign", nullable = false)
    })
    private Turn turns;

    
    @Column(name = "x", nullable = false)
    private Integer x;

    
    @Column(name = "y", nullable = false)
    private Integer y;

}