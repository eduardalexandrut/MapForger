package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.*;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.Map;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record CampaignDetailDTO(UUID id,
                                String name, String description,
                                String pic,
                                LocalDate createdAt,
                                Map map,
                                UserSummaryDTO master,
                                List<CharacterSummaryDTO> characters,
                                List<NpcSummaryDTO> npcs,
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
                UserSummaryDTO.fromEntity(campaign.getCreator()),

                campaign.getCampaignMembers().stream()
                        .flatMap(campaignMember -> campaignMember.getCampaignActors().stream())
                        .map(CampaignActor::getCharacter)
                        .filter(Objects::nonNull) // Safe guard against NPCs
                        .map(CharacterSummaryDTO::fromEnity)
                        .toList(),

                campaign.getCampaignMembers().stream()
                        .flatMap(campaignMember -> campaignMember.getCampaignActors().stream())
                        .map(CampaignActor::getNpc)
                        .filter(Objects::nonNull) // Safe guard against Characters
                        .map(NpcSummaryDTO::fromEnity)
                        .toList(),

                campaign.getCampaignMembers().stream()
                        .flatMap(m -> m.getCampaignActors().stream())
                        .map(CampaignActor::getCharacter)
                        .filter(Objects::nonNull)
                        .map(Character::getCreator)
                        .filter(Objects::nonNull)
                        .distinct() // Avoid duplicate players if they have multiple characters
                        .map(UserSummaryDTO::fromEntity)
                        .toList(),
                campaign.getCampaignMembers().stream().map(CampaignMemberDetailDTO::fromEntity).collect(Collectors.toSet())
        );
    }
}
