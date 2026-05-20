package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.Map;
import com.example.mapforgecore.model.entity.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record CampaignSummaryDTO(UUID id, UserSummaryDTO creator, String name, String description, Integer members,
                                 Map map, String pic, LocalDate createdAt) {
    public static CampaignSummaryDTO fromEntity(Campaign campaign) {
        Optional<Set<CampaignMember>> campaignMembers = Optional.ofNullable(campaign.getCampaignMembers());

        if (campaignMembers.isPresent()) {

            return new CampaignSummaryDTO(
                    campaign.getId(),
                    UserSummaryDTO.fromEntity(campaign.getCreator()),
                    campaign.getName(),
                    campaign.getDescription(),
                    campaign.getCampaignMembers().size(),
                    campaign.getMap(),
                    campaign.getPic(),
                    campaign.getCreatedAt()
            );
        }

        return new CampaignSummaryDTO(
                campaign.getId(),
                UserSummaryDTO.fromEntity(campaign.getCreator()),
                campaign.getName(),
                campaign.getDescription(),
                0,
                campaign.getMap(),
                campaign.getPic(),
                campaign.getCreatedAt()
        );
    }

}
