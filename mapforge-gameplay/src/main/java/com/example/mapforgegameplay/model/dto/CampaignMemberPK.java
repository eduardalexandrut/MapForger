package com.example.mapforgegameplay.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
public class CampaignMemberPK implements Serializable {

    @Column(name = "owner")
    private Integer ownerId;

    @Column(name = "campaign")
    private UUID campaignId;
}