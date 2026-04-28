package com.example.mapforgecore.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "campaign_members")
public class CampaignMember {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private CampaignMemberPK id;

    @MapsId("ownerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @MapsId("campaignId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign", nullable = false)
    private Campaign campaign;

    @OneToMany(mappedBy = "campaignMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignActor> campaignActors = new ArrayList<>();

    @Column(name = "role", nullable = false, length = 10)
    private String role;

}