package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.CampaignActor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CampaignActorBootstrapDTO {
    private Integer id;
    private String type;
    private Integer hp;
    private Integer xp;
    private Integer weaponDamage;
    private Integer ownerId;
    private UUID campaignId;

    public static CampaignActorBootstrapDTO fromEntity(CampaignActor actor) {
        return new CampaignActorBootstrapDTO(
                actor.getId(),
                actor.getType(),
                actor.getHp(),
                actor.getXp(),
                // weaponDamage comes from the character
                actor.getCharacter() != null ? actor.getCharacter().getWeaponDamage() : null,
                actor.getCampaignMember().getOwner().getId(),
                actor.getCampaignMember().getCampaign().getId()
        );
    }
}

