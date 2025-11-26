package com.example.mapforgecore.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "attack_actions")
public class AttackAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attacker", nullable = false)
    private CampaignActor attacker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attacked", nullable = false)
    private CampaignActor attacked;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "turn_index", referencedColumnName = "index", nullable = false),
            @JoinColumn(name = "campaign_id", referencedColumnName = "campaign", nullable = false)
    })
    private Turn turns;

    @Column(name = "damage", nullable = false)
    private Integer damage;

}