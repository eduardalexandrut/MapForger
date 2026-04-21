package com.example.mapforgegameplay.service;


import com.example.mapforgegameplay.model.dto.ActionHistoryDTO;
import com.example.mapforgegameplay.model.dto.CampaignHistoryDTO;
import com.example.mapforgegameplay.model.dto.TurnHistoryDTO;
import com.example.mapforgegameplay.model.entity.*;
import com.example.mapforgegameplay.repository.*;
import com.example.mapforgegameplay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final GameSessionRepository gameSessionRepository;
    private final TurnRepository turnRepository;
    private final MovementActionRepository movementActionRepository;
    private final AttackActionRepository attackActionRepository;
    private final DeathActionRepository deathActionRepository;

    @Transactional(transactionManager = "gameplayTransactionManager", readOnly = true)
    public CampaignHistoryDTO getHistory(UUID campaignId) {
        GameSession session = gameSessionRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("No game session found for campaign " + campaignId));

        List<Turn> turns = turnRepository.findByIdCampaignIdOrderByIdIndexAsc(campaignId);

        List<TurnHistoryDTO> turnHistories = turns.stream()
                .map(turn -> buildTurnHistory(turn, campaignId))
                .toList();

        return new CampaignHistoryDTO(
                campaignId,
                session.getStatus(),
                session.getStartedAt(),
                session.getFinishedAt(),
                turns.size(),
                turnHistories
        );
    }

    private TurnHistoryDTO buildTurnHistory(Turn turn, UUID campaignId) {
        int index = turn.getId().getIndex();
        List<ActionHistoryDTO> actions = new ArrayList<>();

        // Movements
        movementActionRepository
                .findByCampaignIdAndTurnIndex(campaignId, index)
                .forEach(m -> actions.add(new ActionHistoryDTO(
                        "MOVE", m.getActorId(),
                        m.getX(), m.getY(),
                        null, null, null
                )));

        // Attacks
        attackActionRepository
                .findByCampaignIdAndTurnIndex(campaignId, index)
                .forEach(a -> actions.add(new ActionHistoryDTO(
                        "ATTACK", a.getAttackerId(),
                        null, null,
                        a.getAttackedId(), a.getDamage(), null
                )));

        // Deaths
        deathActionRepository
                .findByCampaignIdAndTurnIndex(campaignId, index)
                .forEach(d -> actions.add(new ActionHistoryDTO(
                        "DEATH", d.getActorId(),
                        null, null,
                        null, null, d.getKillerId()
                )));

        return new TurnHistoryDTO(index, turn.getActorId(), turn.getCompleted(), actions);
    }
}