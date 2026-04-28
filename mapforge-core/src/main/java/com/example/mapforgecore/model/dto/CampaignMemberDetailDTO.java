package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.CampaignMember;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record CampaignMemberDetailDTO(UUID ownerId, String ownerName, String role, Set<CampaignActorDetailDTO> actors) {
    public static CampaignMemberDetailDTO fromEntity(CampaignMember campaignMember) {
        return new CampaignMemberDetailDTO(
                campaignMember.getCampaign().getId(),
                campaignMember.getOwner().getUsername(),
                campaignMember.getRole(),
                campaignMember.getCampaignActors() == null ?
                        Set.of() :
                        campaignMember.getCampaignActors().stream()
                                .map(CampaignActorDetailDTO::fromEntity)
                                .collect(Collectors.toSet())
        );
    }
}