package com.example.mapforgecore.service;

import com.example.mapforgecore.model.entity.Map;
import com.example.mapforgecore.repository.MapRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MapService {
    private final MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public Set<Map> getAllMaps() {
        return new HashSet<>(mapRepository.findAll());
    }
}
