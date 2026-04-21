package com.example.mapforgegameplay.repository;


import com.example.mapforgegameplay.model.entity.MovementAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MovementActionRepository extends JpaRepository<MovementAction, Integer> {
    List<MovementAction> findByCampaignIdAndTurnIndex(UUID campaignId, Integer turnIndex);

}
