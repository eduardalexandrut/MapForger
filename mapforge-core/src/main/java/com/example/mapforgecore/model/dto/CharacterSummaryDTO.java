package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.constants.Alignment;
import com.example.mapforgecore.constants.Race;
import com.example.mapforgecore.model.entity.Character;

public record CharacterSummaryDTO(
        Integer id,
        String name,
        Race race,
        Alignment alignment
        ) {
    public static  CharacterSummaryDTO fromEnity(Character character) {
        return new CharacterSummaryDTO(
                character.getId(),
                character.getName(),
                character.getRace(),
                character.getAlignment()
        );
    }
}
