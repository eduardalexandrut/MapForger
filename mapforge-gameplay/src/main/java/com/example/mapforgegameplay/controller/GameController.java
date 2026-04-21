package com.example.mapforgegameplay.controller;

import com.example.mapforgegameplay.model.dto.ActionPayloadDTO;
import com.example.mapforgegameplay.model.dto.GameStateDTO;
import com.example.mapforgegameplay.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    // Creator starts the game
    // Client sends to: /app/room.{campaignId}.start
    // Everyone receives on: /topic/room.{campaignId}.state
    @MessageMapping("/room.{campaignId}.start")
    public void startGame(@DestinationVariable String campaignId) {
        gameService.startGame(UUID.fromString(campaignId));
    }

    // Active actor performs a move or attack
    // Client sends to: /app/room.{campaignId}.action
    // Everyone receives on: /topic/room.{campaignId}.action
    @MessageMapping("/room.{campaignId}.action")
    public void handleAction(
            @DestinationVariable String campaignId,
            @Payload ActionPayloadDTO payload) {
        gameService.handleAction(UUID.fromString(campaignId), payload);
    }

    // Active actor ends their turn
    // Client sends to: /app/room.{campaignId}.endTurn
    // Everyone receives on: /topic/room.{campaignId}.state
    @MessageMapping("/room.{campaignId}.endTurn")
    public void endTurn(
            @DestinationVariable String campaignId,
            @Payload Integer actorId) {
        gameService.endTurn(UUID.fromString(campaignId), actorId);
    }

    // Creator ends the game
    // Client sends to: /app/room.{campaignId}.finish
    @MessageMapping("/room.{campaignId}.finish")
    public void finishGame(@DestinationVariable String campaignId) {
        gameService.finishGame(UUID.fromString(campaignId));
    }
}
