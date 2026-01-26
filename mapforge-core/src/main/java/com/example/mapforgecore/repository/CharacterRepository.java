package com.example.mapforgecore.repository;

import com.example.mapforgecore.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    public Optional<Character> findByName(String name);

    public Optional<Character> findById(Integer id);

    public Set<Character> findAllByCreatorId(Integer id);
}
