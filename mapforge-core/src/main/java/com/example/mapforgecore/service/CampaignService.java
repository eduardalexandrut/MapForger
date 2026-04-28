package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.*;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.Map;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.CampaignMemberRepository;
import com.example.mapforgecore.repository.CampaignRepository;
import com.example.mapforgecore.repository.MapRepository;
import com.example.mapforgecore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMemberRepository campaignMemberRepository;
    private final MapRepository mapRepository;
    private CampaignMemberService campaignMemberService;
    private CampaignActorService campaignActorService;


    public CampaignSummaryDTO createCampaign(CampaignFormDTO campaignFormDTO) {
        User creator = userRepository.findById(campaignFormDTO.creatorId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Map map = mapRepository.findById(campaignFormDTO.mapId())
                .orElseThrow(() -> new EntityNotFoundException("Map not found"));

        Campaign campaign = new Campaign(campaignFormDTO.name(), campaignFormDTO.description(), creator, map, campaignFormDTO.pic());
        Campaign saved = campaignRepository.save(campaign);

        return CampaignSummaryDTO.fromEntity(saved);
    }

    public Set<CampaignSummaryDTO> findAllCampaignSummaries() {
        return campaignRepository.findAll().stream()
                .map(CampaignSummaryDTO::fromEntity)
                .collect(Collectors.toSet());
    }

    public CampaignDetailDTO updateCampaign(String id, CampaignDetailDTO updatedCampaign) {
        return campaignRepository.findById(id)
                .map(oldCampaign -> {
                    oldCampaign.setName(Optional.ofNullable(updatedCampaign.name()).orElse(oldCampaign.getName()));
                    oldCampaign.setDescription(Optional.ofNullable(updatedCampaign.description()).orElse(oldCampaign.getDescription()));
                    oldCampaign.setMap(Optional.ofNullable(updatedCampaign.map()).orElse(oldCampaign.getMap()));

                    Campaign savedCampaign = campaignRepository.save(oldCampaign);

                    return CampaignDetailDTO.fromEntity(savedCampaign);
                }).orElseThrow(() -> new EntityNotFoundException("Campaign not found"));
    }


    //FIXME set value null to fks
    public Optional<CampaignSummaryDTO> deleteCampaign(String id) {
        Optional<Campaign> campaign = campaignRepository.findById(id);

        if (campaign.isEmpty()) {
            return Optional.empty();
        }

        campaignRepository.deleteById(id);
        return Optional.of(CampaignSummaryDTO.fromEntity(campaign.get()));
    }

    @Transactional
    public Optional<CampaignActorBootstrapDTO> joinCampaign(
            String campaignId, Integer userId, Integer characterId) {

        // 1. Create member first
        Optional<CampaignMemberDetailDTO> member = campaignMemberService
                .createCampaignMember(campaignId, userId, characterId);

        if (member.isEmpty()) return Optional.empty();

        // 2. Then create actor
        return campaignActorService
                .createCampaignActor(campaignId, userId, characterId);
    }
}
