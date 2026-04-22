package com.example.mapforgecore.service;

import com.example.mapforgecore.model.entity.Map;
import com.example.mapforgecore.repository.MapRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class MapService {
    private final MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public Set<Map> getAllMaps() {
        return new HashSet<>(mapRepository.findAll());
    }

    public Map getMapById(Integer id) {
        return mapRepository.getReferenceById(id);
    }

    public Map getMapByCampaignId(UUID campaignId) {
        return mapRepository.getMapByCampaignId(campaignId);
    }
}
