package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.CampaignActor;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.Npc;

public record CampaignActorDetailDTO(
        Integer id,
//        UUID campaignId,
        String type,
        Integer xp,
        Integer hp,
        Character character,
        Npc npc
) {
    public static CampaignActorDetailDTO fromEntity(CampaignActor actor) {
        return new CampaignActorDetailDTO(
                actor.getId(),
//                actor.getCampaign() != null ? actor.getCampaign().getId() : null,
                actor.getType(),
                actor.getXp(),
                actor.getHp(),
                actor.getCharacter(),
                actor.getNpc()
        );
    }
}