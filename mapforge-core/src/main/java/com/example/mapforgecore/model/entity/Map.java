package com.example.mapforgecore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "maps")
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "width", nullable = false)
    private Integer width;
    
    @Column(name = "height", nullable = false)
    private Integer height;

}