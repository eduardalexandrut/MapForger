package com.example.mapforgecore.controller;

import com.example.mapforgecore.model.entity.Map;
import com.example.mapforgecore.repository.MapRepository;
import com.example.mapforgecore.service.MapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/maps")
public class MapController {
    private MapRepository mapRepository;
    private MapService mapService;

    public MapController(MapRepository mapRepository, MapService mapService) {
        this.mapRepository = mapRepository;
        this.mapService = mapService;
    }

    @GetMapping()
    public Set<Map> getAllMaps() {
        return mapService.getAllMaps();
    }

    @GetMapping("/{id}")
    public Map getMapById(@PathVariable Integer id) {
        return mapService.getMapById(id);
    }

    @GetMapping("/{campaignId}/map")
    public Map getMapByCampaignId(@PathVariable UUID campaignId) {
        return mapService.getMapByCampaignId(campaignId);
    }
}
