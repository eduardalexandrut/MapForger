package com.example.mapforgegameplay.repository;


import com.example.mapforgegameplay.model.entity.MovementAction;
import com.example.mapforgegameplay.model.entity.Turn;
import com.example.mapforgegameplay.model.entity.TurnPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TurnRepository extends JpaRepository<Turn, TurnPK> {
    List<Turn> findByIdCampaignIdOrderByIdIndexAsc(UUID campaignId);
    List<MovementAction> findByCampaignIdAndTurnIndex(UUID campaignId, Integer turnIndex);

}