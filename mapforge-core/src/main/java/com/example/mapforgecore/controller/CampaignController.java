package com.example.mapforgecore.controller;

import com.example.mapforgecore.model.dto.*;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.CampaignActor;
import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.CampaignActorRepository;
import com.example.mapforgecore.repository.CampaignMemberRepository;
import com.example.mapforgecore.repository.CampaignRepository;
import com.example.mapforgecore.repository.UserRepository;
import com.example.mapforgecore.service.CampaignActorService;
import com.example.mapforgecore.service.CampaignMemberService;
import com.example.mapforgecore.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;
    private final CampaignActorService campaignActorService;
    private final CampaignMemberService campaignMemberService;


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

    // POST /api/v1/campaigns/{id}/join
    @PostMapping("/{id}/testjoin")
    public ResponseEntity<CampaignActorBootstrapDTO> joinCampaign(
            @PathVariable String id,
            @RequestBody JoinCampaignRequestDTO request) {

            return  campaignService.joinCampaign(id, request.userId(), request.characterId())
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET/api/v1/campaigns/{id}/is-member
    @GetMapping("/{id}/is-member")
    public ResponseEntity<Boolean> isCampaignMember(@PathVariable String id, @RequestParam Integer userId) {
        return ResponseEntity.ok(this.campaignService.isCampaignMember(id, userId));
//                .map(ResponseEntity::ok)
//                .orElse(() -> ResponseEntity.notFound().build());
    }
}
