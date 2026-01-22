package com.example.mapforgecore.repository;

import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MapRepository extends JpaRepository<com.example.mapforgecore.model.entity.Map, Integer> {


    public Optional<Map> findById(Integer id);
}
