package com.example.mapforgegameplay;


import com.example.mapforgegameplay.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameplayTestDataHelper {

    @Autowired private GameSessionRepository gameSessionRepository;
    @Autowired private TurnRepository turnRepository;
    @Autowired private CampaignActorRepository campaignActorRepository;
    @Autowired private AttackActionRepository attackActionRepository;
    @Autowired private MovementActionRepository movementActionRepository;
    @Autowired private DeathActionRepository deathActionRepository;

    public void clearAll() {
        deathActionRepository.deleteAll();
        attackActionRepository.deleteAll();
        movementActionRepository.deleteAll();
        turnRepository.deleteAll();
        gameSessionRepository.deleteAll();
        campaignActorRepository.deleteAll();
    }
}