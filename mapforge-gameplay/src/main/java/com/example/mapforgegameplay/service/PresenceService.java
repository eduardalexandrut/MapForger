package com.example.mapforgegameplay.service;

import com.example.mapforgegameplay.model.entity.SessionPresence;
import com.example.mapforgegameplay.repository.SessionPresenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
public class PresenceService {
    private final SessionPresenceRepository sessionPresenceRepository;

    public PresenceService(SessionPresenceRepository sessionPresenceRepository) {
        this.sessionPresenceRepository = sessionPresenceRepository;
    }

    @Transactional
    public void addPlayer(String campaignId, String username) {
        UUID campaignIdUUID = UUID.fromString(campaignId);

        // Avoid duplicates
        if (!sessionPresenceRepository.existsByCampaignIdAndUsername(campaignIdUUID, username)) {
            SessionPresence presence = new SessionPresence();
            presence.setCampaignId(campaignIdUUID);
            presence.setUsername(username);
            presence.setJoinedAt(LocalDateTime.now());
            sessionPresenceRepository.save(presence);
        }
    }

    @Transactional
    public void removePlayer(String campaignId, String username) {
       sessionPresenceRepository.deleteByCampaignIdAndUsername(UUID.fromString(campaignId), username);
    }

    public Set<String> getOnlinePlayers(String campaignId) {
        return this.sessionPresenceRepository.findByCampaignId(UUID.fromString(campaignId)).stream()
                .map(SessionPresence::getUsername)
                .collect(Collectors.toSet());
    }
}
