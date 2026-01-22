package com.example.mapforgecore.controller;

import com.example.mapforgecore.model.dto.CampaignDetailDTO;
import com.example.mapforgecore.model.dto.CampaignFormDTO;
import com.example.mapforgecore.model.dto.CampaignMemberDetailDTO;
import com.example.mapforgecore.model.dto.CampaignSummaryDTO;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.CampaignRepository;
import com.example.mapforgecore.repository.UserRepository;
import com.example.mapforgecore.service.CampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;

    private final CampaignService campaignService;

    public CampaignController(CampaignRepository campaignRepository, CampaignService campaignService, UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignService = campaignService;
    }

    @PostMapping
    public ResponseEntity<CampaignSummaryDTO> createCampaign(@RequestBody CampaignFormDTO campaignFormDTO) {
        return ResponseEntity.ok(campaignService.createCampaign(campaignFormDTO));
    }

    // GET /api/campaigns → all campaigns
    @GetMapping
    public Set<CampaignSummaryDTO> getAllCampaigns() {
        return campaignService.findAllCampaignSummaries();
    }

    // GET /api/campaigns/{id} → single campaigns by id
    @GetMapping("/{id}")
    public CampaignDetailDTO getCampaignById(@PathVariable String id) {
        return campaignRepository.findById(id).map(CampaignDetailDTO::fromEntity).orElse(null);
    }

    // GET /api/v1/campaigns/{id}/campaign_members -> members of a campaign
    @GetMapping("/{id}/campaign_members")
    public Set<CampaignMemberDetailDTO> getCampaignMembersByCampaignId(@PathVariable String id) {
        return campaignRepository.findById(id).stream()
                .flatMap(campaign -> campaign.getCampaignMembers().stream().map(CampaignMemberDetailDTO::fromEntity))
                .collect(Collectors.toSet());
    }

    // PUT /api/v1/campaigns/{id} -> update a campaign
    @PutMapping("/{id}")
    public ResponseEntity<CampaignDetailDTO> updateCampaign(@PathVariable String id, @RequestBody CampaignDetailDTO campaign) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, campaign));
    }

    // DELETE /api/v1/campaigns/{id} -> delete a campaign
    @DeleteMapping("/{id}")
    public ResponseEntity<CampaignSummaryDTO> deleteCampaign(@PathVariable String id) {
        return campaignService.deleteCampaign(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
