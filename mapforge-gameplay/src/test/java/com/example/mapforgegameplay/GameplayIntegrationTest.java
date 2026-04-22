package com.example.mapforgegameplay;

import com.example.mapforgegameplay.mock.MockCoreClientConfig;
import com.example.mapforgegameplay.mock.MockMessagingConfig;
import com.example.mapforgegameplay.model.dto.ActionPayloadDTO;
import com.example.mapforgegameplay.model.entity.*;
import com.example.mapforgegameplay.repository.*;
import com.example.mapforgegameplay.service.CoreClient;
import com.example.mapforgegameplay.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Import({/*MockCoreClientConfig.class,*/ MockMessagingConfig.class})
class GameplayIntegrationTest extends BaseGameplayIntegrationTest {
    @MockitoBean
    private CoreClient coreClient;

    @Autowired private GameService gameService;
    @Autowired private GameSessionRepository gameSessionRepository;
    @Autowired private TurnRepository turnRepository;
    @Autowired private CampaignActorRepository campaignActorRepository;
    @Autowired private AttackActionRepository attackActionRepository;
    @Autowired private MovementActionRepository movementActionRepository;
    @Autowired private DeathActionRepository deathActionRepository;
    @Autowired private GameplayTestDataHelper testDataHelper;

    // Map dimensions for boundary tests
    private static final int MAP_WIDTH = 20;
    private static final int MAP_HEIGHT = 20;

    private UUID campaignId;

    @BeforeEach
    void setUp() {
        // Instead of a separate class, define your "Predefined Map" here
        Map mockMap = new Map(1, "Arena", "Desc", 20, 20, "pic.png", null);

        // This is the missing link that was causing your NPE earlier
        Mockito.when(coreClient.getMapByCampaignId(any(UUID.class)))
                .thenReturn(mockMap);

        // If you need actors too:
        Mockito.when(coreClient.getActorsForCampaign(any(UUID.class)))
                .thenReturn(List.of(/* your actor bootstrap DTOs */));
        testDataHelper.clearAll();
        campaignId = UUID.randomUUID();
        // Manually insert the "Predefined Data"
        CampaignActor a1 = new CampaignActor(1, "CHARACTER", 100, 15,  15, 1,campaignId);
        CampaignActor a2 = new CampaignActor(2, "CHARACTER", 80, 20, 15, 1,campaignId);
        CampaignActor a3 = new CampaignActor(3, "CHARACTER", 60, 10, 15, 1,campaignId);

        campaignActorRepository.saveAll(List.of(a1, a2, a3));
        gameService.startGame(campaignId);
    }

    // ---------------------------------------------------------------
    // MOVEMENT TESTS
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Valid move — actor moves to empty cell within map bounds")
    void shouldAllowValidMove() {
        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("MOVE");
        payload.setX(3);
        payload.setY(5);

        assertThatNoException().isThrownBy(() ->
                gameService.handleAction(campaignId, payload));

        List<MovementAction> moves = movementActionRepository.findAll();
        assertThat(moves).hasSize(1);
        assertThat(moves.get(0).getX()).isEqualTo(3);
        assertThat(moves.get(0).getY()).isEqualTo(5);
    }

    @Test
    @DisplayName("Invalid move — actor moves outside map bounds")
    void shouldRejectMoveOutsideMapBounds() {
        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("MOVE");
        payload.setX(MAP_WIDTH + 1);   // outside
        payload.setY(5);

        assertThatThrownBy(() -> gameService.handleAction(campaignId, payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("out of bounds");
    }

    @Test
    @DisplayName("Invalid move — actor moves to negative coordinates")
    void shouldRejectMoveToNegativeCoordinates() {
        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("MOVE");
        payload.setX(-1);
        payload.setY(0);

        assertThatThrownBy(() -> gameService.handleAction(campaignId, payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("out of bounds");
    }

    @Test
    @DisplayName("Invalid move — target cell already occupied by another actor")
    void shouldRejectMoveToOccupiedCell() {
        // Move actor 1 to (3, 5)
        ActionPayloadDTO first = new ActionPayloadDTO();
        first.setActorId(1);
        first.setType("MOVE");
        first.setX(3);
        first.setY(5);
        gameService.handleAction(campaignId, first);
        gameService.endTurn(campaignId, 1);

        // Actor 2 tries to move to the same cell
        ActionPayloadDTO second = new ActionPayloadDTO();
        second.setActorId(2);
        second.setType("MOVE");
        second.setX(3);
        second.setY(5);

        assertThatThrownBy(() -> gameService.handleAction(campaignId, second))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("occupied");
    }

    @Test
    @DisplayName("Invalid move — actor tries to act out of turn")
    void shouldRejectActionOutOfTurn() {
        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(2);          // actor 2, but it's actor 1's turn
        payload.setType("MOVE");
        payload.setX(3);
        payload.setY(5);

        assertThatThrownBy(() -> gameService.handleAction(campaignId, payload))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not actor 2's turn");
    }

    // ---------------------------------------------------------------
    // ATTACK TESTS
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Attack calculation — damage equals attacker weaponDamage")
    void shouldCalculateDamageCorrectly() {
        // Actor 1 has weaponDamage=15, actor 2 has hp=80
        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("ATTACK");
        payload.setTargetId(2);

        gameService.handleAction(campaignId, payload);

        List<AttackAction> attacks = attackActionRepository.findAll();
        assertThat(attacks).hasSize(1);
        assertThat(attacks.get(0).getDamage()).isEqualTo(15);

        // Verify hp was reduced
        CampaignActor target = campaignActorRepository.findById(2).orElseThrow();
        assertThat(target.getHp()).isEqualTo(65); // 80 - 15
    }

    @Test
    @DisplayName("Attack — hp never goes below zero")
    void shouldCapHpAtZero() {
        // Set actor 2 hp to 5 (less than weaponDamage of actor 1 which is 15)
        CampaignActor weakActor = campaignActorRepository.findById(2).orElseThrow();
        weakActor.setHp(5);
        campaignActorRepository.save(weakActor);

        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("ATTACK");
        payload.setTargetId(2);

        gameService.handleAction(campaignId, payload);

        CampaignActor target = campaignActorRepository.findById(2).orElseThrow();
        assertThat(target.getHp()).isEqualTo(0);  // not negative
    }

    // ---------------------------------------------------------------
    // DEATH TESTS
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Death — DeathAction is persisted when hp reaches zero")
    void shouldPersistDeathActionOnKill() {
        CampaignActor weakActor = campaignActorRepository.findById(2).orElseThrow();
        weakActor.setHp(5);
        campaignActorRepository.save(weakActor);

        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("ATTACK");
        payload.setTargetId(2);

        gameService.handleAction(campaignId, payload);

        List<DeathAction> deaths = deathActionRepository.findAll();
        assertThat(deaths).hasSize(1);
        assertThat(deaths.get(0).getActorId()).isEqualTo(2);
        assertThat(deaths.get(0).getKillerId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Death — dead actor is removed from turn order")
    void shouldRemoveDeadActorFromTurnOrder() {
        CampaignActor weakActor = campaignActorRepository.findById(2).orElseThrow();
        weakActor.setHp(5);
        campaignActorRepository.save(weakActor);

        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(1);
        payload.setType("ATTACK");
        payload.setTargetId(2);

        gameService.handleAction(campaignId, payload);

        GameSession session = gameSessionRepository.findById(campaignId).orElseThrow();
        assertThat(session.getTurnOrder()).doesNotContain(2);
        assertThat(session.getTurnOrder()).hasSize(2); // was 3, now 2
    }

    @Test
    @DisplayName("Death — game finishes when last actor dies")
    void shouldFinishGameWhenLastActorDies() {
        // Kill actors 2 and 3 first, leaving only actor 1
        killActor(2, 1);
        gameService.endTurn(campaignId, 1);
        killActor(3, 1);

        GameSession session = gameSessionRepository.findById(campaignId).orElseThrow();
        assertThat(session.getStatus()).isEqualTo(GameStatus.FINISHED);
    }

    // ---------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------

    private void killActor(Integer targetId, Integer attackerId) {
        CampaignActor actor = campaignActorRepository.findById(targetId).orElseThrow();
        actor.setHp(1);
        campaignActorRepository.save(actor);

        ActionPayloadDTO payload = new ActionPayloadDTO();
        payload.setActorId(attackerId);
        payload.setType("ATTACK");
        payload.setTargetId(targetId);
        gameService.handleAction(campaignId, payload);
    }
}
