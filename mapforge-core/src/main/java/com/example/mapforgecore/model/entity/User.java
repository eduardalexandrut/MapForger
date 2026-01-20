package com.example.mapforgecore.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "pic")
    private Byte pic;

    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Character> characters;

    public User(Integer id) {
        this.id = id;
    }

    public User() {

    }

    public User(Integer creatorId, String creatorUsername, Byte pic, LocalDate joinedDate) {
        this.id = creatorId;
        this.username = creatorUsername;
        this.pic = pic;
        this.joinedDate = joinedDate;
    }
}