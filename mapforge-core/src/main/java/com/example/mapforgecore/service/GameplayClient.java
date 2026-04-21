package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.CampaignActorRuntimeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "mapforge-gameplay")
public interface GameplayClient {

    @GetMapping("/campaigns/{campaignId}/actors/runtime")
    List<CampaignActorRuntimeDTO> getRuntimeActors(@PathVariable UUID campaignId);
}
