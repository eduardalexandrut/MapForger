package com.example.mapforgegameplay.repository;


import com.example.mapforgegameplay.model.entity.AttackAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttackActionRepository extends JpaRepository<AttackAction, Integer> {
    List<AttackAction> findByCampaignIdAndTurnIndex(UUID campaignId, Integer turnIndex);

}