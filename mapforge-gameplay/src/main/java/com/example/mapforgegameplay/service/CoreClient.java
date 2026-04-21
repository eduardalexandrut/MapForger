package com.example.mapforgegameplay.service;

import com.example.mapforgegameplay.model.dto.CampaignActorBootstrapDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "mapforge-core")
public interface CoreClient {

    @GetMapping("/api/v1/campaigns/{campaignId}/actors")
    List<CampaignActorBootstrapDTO> getActorsForCampaign(@PathVariable UUID campaignId);
}