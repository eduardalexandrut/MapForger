package com.example.mapforgecore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "map", nullable = false)
    private Map map;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.EAGER)
    private Set<CampaignMember> campaignMembers;

   // private Set<CampaignActor> campaignActors;
    @Column(name = "pic")
    private String pic;

    @Column(name = "created_at")
    private LocalDate createdAt;

}