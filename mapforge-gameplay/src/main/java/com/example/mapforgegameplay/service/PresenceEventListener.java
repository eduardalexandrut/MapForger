package com.example.mapforgegameplay.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class PresenceEventListener {

    private final PresenceService presenceService;
    private final SimpMessageSendingOperations messagingTemplate;

    public PresenceEventListener(PresenceService presenceService, SimpMessageSendingOperations messagingTemplate) {
        this.presenceService = presenceService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) sha.getSessionAttributes().get("username");
        String campaignId = (String) sha.getSessionAttributes().get("campaignId");

        if (username != null && campaignId != null) {
            presenceService.removePlayer(campaignId, username);
            // Broadcast updated list to the specific campaign topic
            messagingTemplate.convertAndSend("/topic/room." + campaignId + ".presence",
                    presenceService.getOnlinePlayers(campaignId));
        }
    }
}