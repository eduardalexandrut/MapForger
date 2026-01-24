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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "map", nullable = false)
    private Map map;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.EAGER)
    private Set<CampaignMember> campaignMembers;

    @Column(name = "pic")
    private String pic;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDate createdAt;

    public Campaign(String name, String description, User creator, Map map, String pic) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.map = map;
        this.pic = pic;
    }

    public Campaign() {

    }
}