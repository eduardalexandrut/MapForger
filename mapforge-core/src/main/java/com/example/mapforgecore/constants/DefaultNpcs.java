package com.example.mapforgecore.constants;

import lombok.Getter;

@Getter
public enum DefaultNpcs {
    HUMANOID(1,15, 30, 8, NpcType.HUMANOID),
    BEAST(2, 12, 40, 12, NpcType.BEAST),
    UNDEAD(3, 18, 20, 15, NpcType.UNDEAD);

    private final int id;
    private final int armor;
    private final int speed;
    private final int weaponDamage;
    private final NpcType type;

    DefaultNpcs(int id, int armor, int speed, int weaponDamage, NpcType type) {
        this.id = id;
        this.armor = armor;
        this.speed = speed;
        this.weaponDamage = weaponDamage;
        this.type = type;
    }

}
