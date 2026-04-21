package com.example.mapforgecore.repository;

import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.CampaignActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignActorRepository extends JpaRepository<CampaignActor, Integer> {
    @Query("""
         SELECT DISTINCT cm.campaign
         FROM CampaignActor ca
         JOIN ca.campaignMember cm
         WHERE ca.character.id = :characterId
    """)
    List<Campaign> findCampaignsByCharacterId(@Param("characterId") Integer characterId);

    @Query("""
    SELECT ca FROM CampaignActor ca
    JOIN FETCH ca.campaignMember cm
    JOIN FETCH cm.campaign c
    JOIN FETCH cm.owner
    LEFT JOIN FETCH ca.character
    WHERE c.id = :campaignId
    """)
    List<CampaignActor> findByCampaignId(@Param("campaignId") UUID campaignId);
}
