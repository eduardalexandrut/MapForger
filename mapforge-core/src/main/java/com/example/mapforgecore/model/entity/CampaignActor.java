package com.example.mapforgecore.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "campaign_actors")
public class CampaignActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "xp", nullable = false)
    private Integer xp;
    
    @Column(name = "hp", nullable = false)
    private Integer hp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "owner", referencedColumnName = "owner"),
            @JoinColumn(name = "campaign", referencedColumnName = "campaign")
    })
    private CampaignMember campaignMember;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "character")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "campaignActors"})
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "npc")
    private Npc npc;
}