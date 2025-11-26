package com.example.mapforgecore.model.entity;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer armor;

    private Integer speed;

    private Integer weaponDamage;
}
