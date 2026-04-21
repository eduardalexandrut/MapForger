package com.example.mapforgegameplay.repository;

import com.example.mapforgegameplay.model.entity.CampaignActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CampaignActorRepository extends JpaRepository<CampaignActor, Integer> {

    List<CampaignActor> findByCampaignId(UUID campaignId);

    @Modifying
    @Transactional
    @Query("UPDATE CampaignActor a SET a.hp = :hp WHERE a.id = :id")
    void updateHp(@Param("id") Integer id, @Param("hp") Integer hp);
}