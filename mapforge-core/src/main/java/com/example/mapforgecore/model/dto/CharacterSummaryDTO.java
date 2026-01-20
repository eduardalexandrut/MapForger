package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.constants.Alignment;
import com.example.mapforgecore.constants.Race;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;

import java.time.LocalDate;

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
        UserSummaryDTO creatorSummary
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
                new UserSummaryDTO(character.getCreator().getId(), character.getCreator().getUsername(),
                        character.getCreator().getPic(), character.getCreator().getJoinedDate())
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
                new User(characterDTO.creatorSummary.id(), characterDTO.creatorSummary.username(),
                        characterDTO.creatorSummary.pic(), characterDTO.creatorSummary.joinedDate())
        );
    }
}
