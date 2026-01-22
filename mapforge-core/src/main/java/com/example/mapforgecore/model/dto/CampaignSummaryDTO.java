package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.Campaign;

import java.time.LocalDate;
import java.util.UUID;

public record CampaignSummaryDTO(UUID id, String name, String description, Integer members, String pic, LocalDate createdAt) {
    public static CampaignSummaryDTO fromEntity(Campaign campaign) {
        return new CampaignSummaryDTO(
                campaign.getId(),
                campaign.getName(),
                campaign.getDescription(),
                campaign.getCampaignMembers().size(),
                campaign.getPic(),
                campaign.getCreatedAt()
        );
    }
}
