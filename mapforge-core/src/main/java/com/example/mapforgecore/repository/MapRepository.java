package com.example.mapforgecore.repository;

import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MapRepository extends JpaRepository<com.example.mapforgecore.model.entity.Map, Integer> {


    public Optional<Map> findById(Integer id);

    @Query("SELECT m FROM Map m JOIN Campaign c ON c.map.id = m.id WHERE c.id = :campaignId")
    Optional<Map> findByCampaignId(@Param("campaignId") UUID campaignId);
}
