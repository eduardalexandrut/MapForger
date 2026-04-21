package com.example.mapforgegameplay.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Getter
@Immutable
@Entity
@Table(name = "campaign_members")
public class CampaignMember {

    @EmbeddedId
    private CampaignMemberPK id;

    @Column(name = "role")
    private String role;
}