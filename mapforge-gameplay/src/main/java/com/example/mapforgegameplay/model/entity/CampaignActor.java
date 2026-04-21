package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "campaign_actors")
public class CampaignActor {

    @Id
    @Column(name = "id")
    private Integer id;             // same ID as in Core — no generation, we copy it

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "hp", nullable = false)
    private Integer hp;

    @Column(name = "xp", nullable = false)
    private Integer xp;

    @Column(name = "weapon_damage", nullable = false)
    private Integer weaponDamage;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;        // plain ref to Core's users table

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;        // plain ref to Core's campaigns table

    @Column(name = "weapon_damage")
    private Integer weaponDamage;
}