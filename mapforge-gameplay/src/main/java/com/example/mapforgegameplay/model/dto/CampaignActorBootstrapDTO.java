package com.example.mapforgegameplay.model.dto;

import com.example.mapforgegameplay.model.entity.CampaignActor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
                actor.getWeaponDamage(),
                actor.getSpeed(),
                actor.getOwnerId(),
                actor.getCampaignId(),
                actor.getX(),
                actor.getY()
        );
    }
}

