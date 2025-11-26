package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.Campaign;

import java.util.UUID;

public record CampaignSummaryDTO(UUID id, String name, String description, Integer members) {
    public static CampaignSummaryDTO fromEntity(Campaign campaign) {
        return new CampaignSummaryDTO(
                campaign.getId(),
                campaign.getName(),
                campaign.getDescription(),
                campaign.getCampaignMembers().size()
        );
    }
}
