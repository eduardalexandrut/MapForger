package com.example.mapforgegameplay.repository;


import com.example.mapforgegameplay.model.entity.DeathAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeathActionRepository extends JpaRepository<DeathAction, Integer> {
    List<DeathAction> findByCampaignIdAndTurnIndex(UUID campaignId, Integer turnIndex);

}