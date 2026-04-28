package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.CampaignMemberDetailDTO;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.CampaignMemberPK;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignMemberService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMemberRepository campaignMemberRepository;
    private final CharacterRepository characterRepository;
    private final CampaignActorRepository campaignActorRepository;

    public Optional<CampaignMemberDetailDTO> createCampaignMember(String campaignId, Integer userId, Integer characterId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Campaign> campaign = campaignRepository.findById(String.valueOf(campaignId));

        if (user.isEmpty() || campaign.isEmpty()) {
            return Optional.empty();
        }

        // Check if already a member
        boolean alreadyMember = campaignMemberRepository
                .existsByIdOwnerIdAndIdCampaignId(userId, UUID.fromString(campaignId));

        if (alreadyMember) {
            return Optional.empty(); // or return existing member
        }

        CampaignMemberPK pk = new CampaignMemberPK();
        pk.setOwnerId(userId);
        pk.setCampaignId(UUID.fromString(campaignId));

        CampaignMember member = new CampaignMember();
        member.setId(pk);
        member.setOwner(user.get());
        member.setCampaign(campaign.get());
        member.setRole("PLAYER");

        return Optional.of(CampaignMemberDetailDTO.fromEntity(
                campaignMemberRepository.save(member)));
    }
}
