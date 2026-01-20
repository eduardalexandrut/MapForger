package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.constants.Alignment;
import com.example.mapforgecore.constants.Race;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;

import java.util.Set;

public record CharacterDetailDTO(
        Integer id,
        String name,
        Race race,
        Alignment alignment,
        Integer armor,
        Integer speed,
        Integer weaponDamage,
        String  description,
        String backstory,
        UserSummaryDTO user,
        Set<CampaignSummaryDTO> campaigns
) {
    public static  CharacterDetailDTO fromEnity(Character character, Set<CampaignSummaryDTO> campaigns) {
        return new CharacterDetailDTO(
                character.getId(),
                character.getName(),
                character.getRace(),
                character.getAlignment(),
                character.getArmor(),
                character.getSpeed(),
                character.getWeaponDamage(),
                character.getDescription(),
                character.getBackstory(),
                UserSummaryDTO.fromEntity(character.getCreator()),
                campaigns
        );
    }

}
