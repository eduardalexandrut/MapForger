package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.constants.Alignment;
import com.example.mapforgecore.constants.Race;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;

public record CharacterSummaryDTO(
        Integer id,
        String name,
        Race race,
        Alignment alignment,
        Integer armor,
        Integer weaponDamage,
        Integer speed,
        String description,
        String backstory,
        Integer creatorId,
        String creatorUsername
        ) {

    public static  CharacterSummaryDTO fromEnity(Character character) {
        return new CharacterSummaryDTO(
                character.getId(),
                character.getName(),
                character.getRace(),
                character.getAlignment(),
                character.getArmor(),
                character.getWeaponDamage(),
                character.getSpeed(),
                character.getDescription(),
                character.getBackstory(),
                character.getCreator().getId(),
                character.getCreator().getUsername()
        );
    }

    public static Character fromDTO(CharacterSummaryDTO characterDTO) {
        return new Character(
                characterDTO.name,
                characterDTO.alignment,
                characterDTO.race,
                characterDTO.armor,
                characterDTO.weaponDamage,
                characterDTO.speed,
                characterDTO.description,
                characterDTO.backstory,
                new User(characterDTO.creatorId, characterDTO.creatorUsername)
        );
    }
}
