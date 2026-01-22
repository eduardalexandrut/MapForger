package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.*;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.Map;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record CampaignDetailDTO(UUID id, String name, String description,
                                String pic,
                                LocalDate createdAt,
                                Map map,
                                UserSummaryDTO master,
                                List<CharacterSummaryDTO> characters,
                                List<UserSummaryDTO> players,
                                Set<CampaignMemberDetailDTO> members) {
    public static CampaignDetailDTO fromEntity(Campaign campaign) {
        return new CampaignDetailDTO(
                campaign.getId(),
                campaign.getName(),
                campaign.getDescription(),
                campaign.getPic(),
                campaign.getCreatedAt(),
                campaign.getMap(),
                campaign.getCampaignMembers().stream().filter(m -> Objects.equals(m.getRole(), "Master"))
                        .map(m -> m.getCampaignActors().stream().findFirst().get().getCharacter().getCreator())
                        .map(UserSummaryDTO::fromEntity).findAny().orElse(null),
                campaign.getCampaignMembers().stream().flatMap(campaignMember -> campaignMember.getCampaignActors().stream())
                        .map(CampaignActor::getCharacter).map(CharacterSummaryDTO::fromEnity).toList(),
                campaign.getCampaignMembers().stream()
                        .map(m -> m.getCampaignActors().stream().findFirst().get().getCharacter().getCreator())
                        .map(UserSummaryDTO::fromEntity).toList(),
                campaign.getCampaignMembers().stream().map(CampaignMemberDetailDTO::fromEntity).collect(Collectors.toSet())
        );
    }
}
