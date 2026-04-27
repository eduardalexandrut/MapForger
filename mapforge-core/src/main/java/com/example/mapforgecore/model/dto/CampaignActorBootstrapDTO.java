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
    private Integer speed;
    private Integer ownerId;
    private UUID campaignId;
    private Integer x;
    private Integer y;

    public static CampaignActorBootstrapDTO fromEntity(CampaignActor actor) {
        return new CampaignActorBootstrapDTO(
                actor.getId(),
                actor.getType(),
                actor.getHp(),
                actor.getXp(),
                actor.getCharacter() != null ? actor.getCharacter().getWeaponDamage() : 0,
                actor.getCharacter() != null ? actor.getCharacter().getSpeed() : 0,
                actor.getCampaignMember().getOwner().getId(),
                actor.getCampaignMember().getCampaign().getId(),
                actor.getX(),
                actor.getY()
        );
    }
}

