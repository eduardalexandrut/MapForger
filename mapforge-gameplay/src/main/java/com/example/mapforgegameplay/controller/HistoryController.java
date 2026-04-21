package com.example.mapforgegameplay.controller;


import com.example.mapforgegameplay.model.dto.CampaignHistoryDTO;
import com.example.mapforgegameplay.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/{campaignId}/history")
    public ResponseEntity<CampaignHistoryDTO> getHistory(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(historyService.getHistory(campaignId));
    }
}