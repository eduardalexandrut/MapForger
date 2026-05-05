package com.example.mapforgegameplay.service;

import com.example.mapforgegameplay.model.dto.ActionPayloadDTO;
import com.example.mapforgegameplay.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgegameplay.model.dto.GameStateDTO;
import com.example.mapforgegameplay.model.dto.TurnResultDTO;
import com.example.mapforgegameplay.model.entity.*;
import com.example.mapforgegameplay.model.entity.Map;
import com.example.mapforgegameplay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameSessionRepository gameSessionRepository;
    private final TurnRepository turnRepository;
    private final AttackActionRepository attackActionRepository;
    private final MovementActionRepository movementActionRepository;
    private final DeathActionRepository deathActionRepository;
    private final CampaignActorRepository campaignActorRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final CoreClient coreClient;
    private UUID campaignId;

    // ---------------------------------------------------------------
    // START GAME
    // ---------------------------------------------------------------

    @Transactional
    public GameStateDTO startGame(UUID campaignId) {
        if (gameSessionRepository.existsById(campaignId)) {
            GameSession gameSession = gameSessionRepository.findById(campaignId).orElseThrow();
            return buildState(gameSession);

        }

        this.campaignId = campaignId;

        // 1. Fetch actors from MapForge
        List<CampaignActorBootstrapDTO> bootstrapActors = coreClient.getActorsForCampaign(campaignId);


        if (bootstrapActors.isEmpty()) {
            throw new IllegalStateException("No actors found for campaign " + campaignId);
        }

        List<CampaignActor> actors = bootstrapActors.stream().map(dto -> {
            CampaignActor actor = new CampaignActor();
            actor.setId(dto.getId());
            actor.setType(dto.getType());
            actor.setHp(dto.getHp());
            actor.setXp(dto.getXp());
            actor.setWeaponDamage(dto.getWeaponDamage());
            actor.setOwnerId(dto.getOwnerId());
            actor.setSpeed(dto.getSpeed());
            actor.setCampaignId(campaignId);
            return actor;
        }).toList();

        campaignActorRepository.saveAll(actors);

        // 2. Build turn order — simple: just the order they come back from DB
        //    You can shuffle or sort by initiative here later
        List<Integer> turnOrder = actors.stream()
                .map(CampaignActor::getId)
                .toList();

        // 3. Create and persist GameSession
        GameSession session = GameSession.create(campaignId);
        session.start();
        session.getTurnOrder().addAll(turnOrder);
        gameSessionRepository.save(session);

        // 4. Create first Turn
        Integer firstActorId = turnOrder.get(0);
        turnRepository.save(Turn.create(campaignId, 0, firstActorId));

        // 5. Build and broadcast state
        GameStateDTO state = buildState(session);
        messagingTemplate.convertAndSend("/topic/room." + campaignId + ".state", state);

        return state;
    }

    // ---------------------------------------------------------------
    // HANDLE ACTION
    // ---------------------------------------------------------------

    @Transactional
    public void handleAction(UUID campaignId, ActionPayloadDTO payload) {
        GameSession session = getActiveSession(campaignId);
        Turn currentTurn = getCurrentTurn(session);
        Map currentMap = getCurrentMap(campaignId);

        // Validate it's this actor's turn
        if (!currentTurn.getActorId().equals(payload.getActorId())) {
            throw new IllegalStateException("It's not actor " + payload.getActorId() + "'s turn");
        }

        TurnResultDTO result = switch (payload.getType()) {
            case "MOVE" -> {
                if (isMovementValid(payload, currentMap)) {
                    yield handleMove(payload, currentTurn, campaignId);
                } else {
                    throw new IllegalArgumentException("out of bounds");
                }
            }
            case "ATTACK" -> handleAttack(payload, currentTurn, campaignId);
            default -> throw new IllegalArgumentException("Unknown action type: " + payload.getType());
        };

        // Broadcast action result
        messagingTemplate.convertAndSend("/topic/room." + campaignId + ".action", result);
    }

    private boolean isMovementValid(ActionPayloadDTO payload, Map currentMap) {
        final boolean cellIsValid = payload.getX() >= 0 && payload.getX() < currentMap.getWidth()
                && payload.getY() >= 0 && payload.getY() < currentMap.getHeight();

        if (!cellIsValid) {
            throw new IllegalArgumentException("Position out of bounds");
        }

        final boolean cellIsOccupied = campaignActorRepository
                .findByCampaignId(campaignId)
                .stream()
                .filter(c -> !c.getId().equals(payload.getActorId())) // exclude self
                .anyMatch(c -> Objects.equals(c.getX(), payload.getX())
                        && Objects.equals(c.getY(), payload.getY()));

        if (cellIsOccupied) {
            throw new IllegalArgumentException("Cell is already occupied");
        }

        // Speed check — Chebyshev distance (max of x and y deltas, allows diagonals)
        CampaignActor actor = campaignActorRepository.findById(payload.getActorId())
                .orElseThrow(() -> new IllegalArgumentException("Actor not found"));

        // Actor has no position yet (first move) — any valid cell within speed is fine
        if (actor.getX() == null || actor.getY() == null) {
            return true;
        }

        int dx = Math.abs(payload.getX() - actor.getX());
        int dy = Math.abs(payload.getY() - actor.getY());
        int distance = Math.max(dx, dy); // Chebyshev distance

        if (distance > actor.getSpeed()) {
            throw new IllegalArgumentException(
                    "Move exceeds speed limit. Max: " + actor.getSpeed() + ", attempted: " + distance
            );
        }

        return true;
    }

    private Map getCurrentMap(UUID campaignId) {
        return coreClient.getMapByCampaignId(campaignId);
    }

    // ---------------------------------------------------------------
    // END TURN
    // ---------------------------------------------------------------

    @Transactional
    public GameStateDTO endTurn(UUID campaignId, Integer actorId) {
        GameSession session = getActiveSession(campaignId);
        Turn currentTurn = getCurrentTurn(session);

        if (!currentTurn.getActorId().equals(actorId)) {
            throw new IllegalStateException("Actor " + actorId + " cannot end turn — not their turn");
        }

        // Mark current turn complete
        currentTurn.setCompleted(true);
        turnRepository.save(currentTurn);

        // Advance turn index
        int nextIndex = session.getCurrentTurnIndex() + 1;
        List<Integer> order = session.getTurnOrder();
        Integer nextActorId = order.get(nextIndex % order.size());

        session.setCurrentTurnIndex(nextIndex);
        gameSessionRepository.save(session);

        // Create next Turn
        turnRepository.save(Turn.create(campaignId, nextIndex, nextActorId));

        // Broadcast new state
        GameStateDTO state = buildState(session);
        messagingTemplate.convertAndSend("/topic/room." + campaignId + ".state", state);

        return state;
    }

    // ---------------------------------------------------------------
    // FINISH GAME
    // ---------------------------------------------------------------

    @Transactional
    public void finishGame(UUID campaignId) {
        GameSession session = getActiveSession(campaignId);
        session.finish();
        gameSessionRepository.save(session);

        messagingTemplate.convertAndSend("/topic/room." + campaignId + ".state", buildState(session));
    }

    // ---------------------------------------------------------------
    // PRIVATE HELPERS
    // ---------------------------------------------------------------

    private TurnResultDTO handleMove(ActionPayloadDTO payload, Turn turn, UUID campaignId) {
        // Update actor position
        CampaignActor actor = campaignActorRepository.findById(payload.getActorId())
                .orElseThrow(() -> new IllegalArgumentException("Actor not found"));
        actor.setX(payload.getX());
        actor.setY(payload.getY());
        campaignActorRepository.save(actor);

        // Persist movement action
        MovementAction action = new MovementAction();
        action.setActorId(payload.getActorId());
        action.setTurnIndex(turn.getId().getIndex());
        action.setCampaignId(campaignId);
        action.setX(payload.getX());
        action.setY(payload.getY());
        movementActionRepository.save(action);

        return new TurnResultDTO("MOVE", payload.getActorId(),
                payload.getX(), payload.getY(), null, null, false, null, false, 100);
    }

    private GameSession getActiveSession(UUID campaignId) {
        GameSession session = gameSessionRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalStateException("No game session for campaign " + campaignId));
        if (session.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Game is not active for campaign " + campaignId);
        }
        return session;
    }

    private Turn getCurrentTurn(GameSession session) {
        TurnPK pk = new TurnPK(session.getCurrentTurnIndex(), session.getCampaignId());
        return turnRepository.findById(pk)
                .orElseThrow(() -> new IllegalStateException("Current turn not found"));
    }

    public GameStateDTO buildState(GameSession session) {
        List<Integer> order = session.getTurnOrder();
        Integer currentActorId = order.isEmpty() ? null
                : order.get(session.getCurrentTurnIndex() % order.size());

        return new GameStateDTO(
                session.getCampaignId(),
                session.getStatus(),
                session.getCurrentTurnIndex(),
                currentActorId,
                order
        );
    }

    private TurnResultDTO handleAttack(ActionPayloadDTO payload, Turn turn, UUID campaignId) {
        CampaignActor attacker = campaignActorRepository.findById(payload.getActorId())
                .orElseThrow(() -> new IllegalArgumentException("Attacker not found"));

        CampaignActor attacked = campaignActorRepository.findById(payload.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Target not found"));

        int damage = attacker.getWeaponDamage();
        int newHp = Math.max(0, attacked.getHp() - damage);
        campaignActorRepository.updateHp(attacked.getId(), newHp);

        AttackAction action = new AttackAction();
        action.setAttackerId(payload.getActorId());
        action.setAttackedId(payload.getTargetId());
        action.setTurnIndex(turn.getId().getIndex());
        action.setCampaignId(campaignId);
        action.setDamage(damage);
        attackActionRepository.save(action);

        // Check for death
        if (newHp == 0) {
            return handleDeath(payload, turn, campaignId, damage);
        }

        return new TurnResultDTO("ATTACK", payload.getActorId(),
                null, null, payload.getTargetId(), damage, false, null, false, 100);
    }

    private TurnResultDTO handleDeath(ActionPayloadDTO payload, Turn turn, UUID campaignId, int damage) {
        // 1. Persist DeathAction
        DeathAction death = new DeathAction();
        death.setActorId(payload.getTargetId());
        death.setKillerId(payload.getActorId());
        death.setTurnIndex(turn.getId().getIndex());
        death.setCampaignId(campaignId);
        deathActionRepository.save(death);

        // 2. Remove dead actor from turn order
        GameSession session = getActiveSession(campaignId);
        List<Integer> order = session.getTurnOrder();
        int deadIndex = order.indexOf(payload.getTargetId());
        order.remove(payload.getTargetId());

        boolean gameFinished = order.size() <= 1;

        if (gameFinished) {
            // Last actor standing — game over
            session.finish();
            gameSessionRepository.save(session);
            messagingTemplate.convertAndSend("/topic/room." + campaignId + ".state", buildState(session));
        } else {
            // If the dead actor was at or before the current turn index,
            // shift the index so the same actor doesn't get skipped/repeated
            int currentIndex = session.getCurrentTurnIndex();
            if (deadIndex <= currentIndex && currentIndex > 0) {
                session.setCurrentTurnIndex(currentIndex - 1);
            }
            gameSessionRepository.save(session);
        }

        // 3. Broadcast death event separately so the client knows who to spectate
        messagingTemplate.convertAndSend("/topic/room." + campaignId + ".death", payload.getTargetId());

        return new TurnResultDTO("ATTACK", payload.getActorId(),
                null, null, payload.getTargetId(), damage, false, payload.getTargetId(), gameFinished, 100);
    }
}