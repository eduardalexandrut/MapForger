package com.example.mapforgegameplay.controller;

import com.example.mapforgegameplay.model.dto.CampaignActorRuntimeDTO;
import com.example.mapforgegameplay.repository.CampaignActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignActorController {

    private final CampaignActorRepository campaignActorRepository;

    @GetMapping("/{campaignId}/actors/runtime")
    public ResponseEntity<List<CampaignActorRuntimeDTO>> getRuntimeActors(
            @PathVariable UUID campaignId) {
        List<CampaignActorRuntimeDTO> actors = campaignActorRepository
                .findByCampaignId(campaignId)
                .stream()
                .map(a -> new CampaignActorRuntimeDTO(a.getId(), a.getHp(), a.getXp()))
                .toList();
        return ResponseEntity.ok(actors);
    }
}