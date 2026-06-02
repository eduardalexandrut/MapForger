package com.example.mapforgegameplay.repository;

import com.example.mapforgegameplay.model.entity.SessionPresence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionPresenceRepository extends JpaRepository<SessionPresence, Integer> {
    List<SessionPresence> findByCampaignId(UUID campaignId);
    Optional<SessionPresence> findByCampaignIdAndUsername(UUID campaignId, String username);
    void deleteByCampaignIdAndUsername(UUID campaignId, String username);
    boolean existsByCampaignIdAndUsername(UUID campaignId, String username);
}