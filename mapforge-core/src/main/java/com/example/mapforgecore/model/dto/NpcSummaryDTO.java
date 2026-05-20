package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.constants.Alignment;
import com.example.mapforgecore.constants.NpcType;
import com.example.mapforgecore.constants.Race;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.Npc;

import java.time.LocalDate;

public record NpcSummaryDTO(
    Integer id,
    Integer armor,
    Integer weaponDamage,
    Integer speed,
    Integer hp,
    String pic,
    NpcType type
    ) {

        public static  NpcSummaryDTO fromEnity(Npc npc) {
            return new NpcSummaryDTO(
                    npc.getId(),
                    npc.getArmor(),
                    npc.getWeaponDamage(),
                    npc.getSpeed(),
                    npc.getHp(),
                    npc.getPic(),
                    npc.getType()
            );
        }
}
