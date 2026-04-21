package com.example.mapforgegameplay.repository;


import com.example.mapforgegameplay.model.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameSessionRepository extends JpaRepository<GameSession, UUID> {}