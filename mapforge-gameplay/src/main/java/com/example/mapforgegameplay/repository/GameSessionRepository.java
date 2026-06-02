package com.example.mapforgegameplay.repository;


import com.example.mapforgegameplay.model.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GameSessionRepository extends JpaRepository<GameSession, UUID> {
    @Query("SELECT g FROM GameSession g WHERE g.status = 'ACTIVE'")
    List<GameSession> findAllActive();
}