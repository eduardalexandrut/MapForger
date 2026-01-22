package com.example.mapforgecore.model.entity;

import com.example.mapforgecore.constants.Alignment;
import com.example.mapforgecore.constants.Race;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "characters"})
    private User creator;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "armor", nullable = false)
    private Integer armor;
    
    @Column(name = "speed", nullable = false)
    private Integer speed;
    
    @Column(name = "weapon_damage", nullable = false)
    private Integer weaponDamage;

    @Enumerated(EnumType.STRING)
    @Column(name = "race", nullable = false,  columnDefinition = "race_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "alignment", nullable = false, columnDefinition = "alignment_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Alignment alignment;

    @Column(name = "description")
    private String description;

    @Column(name = "backstory")
    private String backstory;

    @Column(name = "pic")
    private String pic;

    @Column(name = "createdAt", insertable = false, updatable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CampaignActor> campaignActors;

    public Character() {}

    public Character(String name, Alignment alignment, Race race, Integer armor, Integer weaponDamage, Integer speed,
                     String description, String backstory, String pic, LocalDate createdAt, User user) {
        this.name = name;
        this.alignment = alignment;
        this.race = race;
        this.armor = armor;
        this.speed = speed;
        this.weaponDamage = weaponDamage;
        this.description = description;
        this.backstory = backstory;
        this.pic = pic;
        this.createdAt = createdAt;
        this.creator = user;
    }
}