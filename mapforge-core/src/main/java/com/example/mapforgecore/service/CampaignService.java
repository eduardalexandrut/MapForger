package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.AuthResponseDTO;
import com.example.mapforgecore.model.dto.CampaignDetailDTO;
import com.example.mapforgecore.model.dto.CampaignSummaryDTO;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Optional<Campaign> createCampaign(Campaign campaign) {
       return Optional.of(campaignRepository.save(campaign));
    }

    public Set<CampaignSummaryDTO> findAllCampaignSummaries() {
        return campaignRepository.findAll().stream()
                .map(CampaignSummaryDTO::fromEntity)
                .collect(Collectors.toSet());
    }

    public Optional<CampaignDetailDTO> updateCampaign(String id, CampaignDetailDTO updatedCampaign) {
        return campaignRepository.findById(id)
                .map(oldCampaign -> {
                    oldCampaign.setName(Optional.ofNullable(updatedCampaign.name()).orElse(oldCampaign.getName()));
                    oldCampaign.setDescription(Optional.ofNullable(updatedCampaign.description()).orElse(oldCampaign.getDescription()));
                    oldCampaign.setMap(Optional.ofNullable(updatedCampaign.map()).orElse(oldCampaign.getMap()));

                    Campaign savedCampaign = campaignRepository.save(oldCampaign);

                    return CampaignDetailDTO.fromEntity(savedCampaign);
                });
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
}
