package com.example.mapforgegameplay.controller;

import com.example.mapforgegameplay.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgegameplay.model.dto.CampaignActorRuntimeDTO;
import com.example.mapforgegameplay.model.dto.GameStateDTO;
import com.example.mapforgegameplay.model.entity.CampaignActor;
import com.example.mapforgegameplay.repository.CampaignActorRepository;
import com.example.mapforgegameplay.repository.GameSessionRepository;
import com.example.mapforgegameplay.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class GameStateController {

    private final GameSessionRepository gameSessionRepository;
    private final GameService gameService;
    private final CampaignActorRepository campaignActorRepository;

    @GetMapping("/{campaignId}/state")
    public ResponseEntity<GameStateDTO> getState(@PathVariable UUID campaignId) {
        return gameSessionRepository.findById(campaignId)
                .map(session -> ResponseEntity.ok(gameService.buildState(session)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{campaignId}/actors")
    public ResponseEntity<List<CampaignActorBootstrapDTO>> getActors(@PathVariable UUID campaignId) {
        final List<CampaignActor> actors = campaignActorRepository.findByCampaignId(campaignId);

        if (actors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actors.stream().map(CampaignActorBootstrapDTO::fromEntity).toList());
    }
}