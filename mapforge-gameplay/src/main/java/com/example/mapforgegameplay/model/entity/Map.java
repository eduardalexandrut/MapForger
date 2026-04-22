package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "maps")
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "pic")
    private String pic;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDate createdAt;

}