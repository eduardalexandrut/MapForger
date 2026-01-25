package com.example.mapforgegameplay.controller;

import com.example.mapforgegameplay.service.PresenceService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Set;

@Controller
//@RequestMapping("/ws")
public class LobbyController {

    private PresenceService presenceService;

    public LobbyController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @MessageMapping("/room.{campaignId}.join")
    @SendTo("/topic/room.{campaignId}.presence")
    public Set<String> join(@DestinationVariable String campaignId, @Payload String username, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", username);
            headerAccessor.getSessionAttributes().put("campaignId", campaignId);
        }

        presenceService.addPlayer(campaignId, username);

        return presenceService.getOnlinePlayers(campaignId);
    }

}
