package com.example.mapforgegameplay.model.dto;

import com.example.mapforgegameplay.model.entity.CampaignActor;
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
    private Integer x;
    private Integer y;

    public static CampaignActorBootstrapDTO fromEntity(CampaignActor actor) {
        return new CampaignActorBootstrapDTO(
                actor.getId(),
                actor.getType(),
                actor.getHp(),
                actor.getXp(),
                actor.getWeaponDamage(),
                actor.getOwnerId(),
                actor.getCampaignId(),
                actor.getX(),
                actor.getY()
        );
    }
}

