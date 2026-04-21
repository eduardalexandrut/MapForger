package com.example.mapforgecore.controller;

import com.example.mapforgecore.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgecore.model.dto.CampaignActorRuntimeDTO;
import com.example.mapforgecore.repository.CampaignActorRepository;
import com.example.mapforgecore.service.GameplayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/actors")
@RequiredArgsConstructor
public class CampaignActorController {

    private final CampaignActorRepository campaignActorRepository;
    private final GameplayClient gameplayClient;

    // Called by Gameplay at startGame
    @GetMapping
    public ResponseEntity<List<CampaignActorBootstrapDTO>> getActors(@PathVariable UUID campaignId) {
        List<CampaignActorBootstrapDTO> actors = campaignActorRepository
                .findByCampaignId(campaignId)
                .stream()
                .map(CampaignActorBootstrapDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(actors);
    }

    // Called by frontend for campaign detail page — fetches live hp/xp from Gameplay
    @GetMapping("/runtime")
    public ResponseEntity<List<CampaignActorRuntimeDTO>> getRuntimeActors(
            @PathVariable UUID campaignId) {
        return ResponseEntity.ok(gameplayClient.getRuntimeActors(campaignId));
    }
}