package com.example.mapforgecore.model.entity;

import com.example.mapforgecore.constants.NpcType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "npcs")
public class Npc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "armor", nullable = false)
    private Integer armor;
    
    @Column(name = "speed", nullable = false)
    private Integer speed;
    
    @Column(name = "weapon_damage", nullable = false)
    private Integer weaponDamage;

    @Column(name = "type", nullable = false)
    private NpcType type;
}