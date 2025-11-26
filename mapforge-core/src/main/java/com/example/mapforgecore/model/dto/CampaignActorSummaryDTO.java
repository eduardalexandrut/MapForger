package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.CampaignActor;

public record CampaignActorSummaryDTO(
        Integer id,
//        UUID campaignId,
       // String type,
        Integer xp,
        Integer hp
) {
    public static CampaignActorSummaryDTO fromEntity(CampaignActor actor) {
        return new CampaignActorSummaryDTO(
                actor.getId(),
//                actor.getCampaign() != null ? actor.getCampaign().getId() : null,
               // actor.getType(),
                actor.getXp(),
                actor.getHp()
        );
    }
}