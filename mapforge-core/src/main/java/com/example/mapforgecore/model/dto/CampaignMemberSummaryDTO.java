package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.CampaignMember;

import java.util.UUID;

public record CampaignMemberSummaryDTO(UUID ownerId, String ownerName, String role) {
    public static CampaignMemberSummaryDTO fromEntity(CampaignMember campaignMember) {
        return new CampaignMemberSummaryDTO(
                campaignMember.getId().getCampaignId(),
                campaignMember.getOwner().getUsername(),
                campaignMember.getRole()
        );
    }
}
