package com.example.mapforgegameplay.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class PresenceService {
    private final Map<String, Set<String>> storage = new ConcurrentHashMap<>();

    public void addPlayer(String campaignId, String username) {
        storage.computeIfAbsent(campaignId, k -> new CopyOnWriteArraySet<>()).add(username);
    }

    public void removePlayer(String campaignId, String username) {
        if (storage.containsKey(campaignId)) {
            storage.get(campaignId).remove(username);
        }
    }

    public Set<String> getOnlinePlayers(String campaignId) {
        return storage.getOrDefault(campaignId, Collections.emptySet());
    }
}
