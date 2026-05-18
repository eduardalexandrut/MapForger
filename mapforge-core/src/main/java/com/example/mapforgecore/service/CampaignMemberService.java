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

        // Convert the String ID to UUID safely
        UUID campaignUuid;
        try {
            campaignUuid = UUID.fromString(campaignId);
        } catch (IllegalArgumentException e) {
            System.err.println("DEBUG: Invalid UUID format: " + campaignId);
            return Optional.empty();
        }

        // Fetch Entities
        Optional<User> user = userRepository.findById(userId);
        Optional<Campaign> campaign = campaignRepository.findById(campaignUuid);

        System.out.println("DEBUG: User found: " + user.isPresent());
        System.out.println("DEBUG: Campaign found: " + campaign.isPresent());

        if (user.isEmpty() || campaign.isEmpty()) {
            System.out.println("DEBUG: Exiting because User or Campaign was missing in DB");
            return Optional.empty();
        }

        // 3. Check for existing membership using the UUID-compatible repository method
        boolean alreadyMember = campaignMemberRepository.existsByIdOwnerIdAndIdCampaignId(userId, campaignUuid);

        if (alreadyMember) {
            System.out.println("DEBUG: User " + userId + " is already a member of campaign " + campaignId);
            // Optional: Fetch and return existing instead of empty
            return campaignMemberRepository.findByIdOwnerIdAndIdCampaignId(userId, campaignUuid)
                    .map(CampaignMemberDetailDTO::fromEntity);
        }

        // 4. Build the Composite Key
        CampaignMemberPK pk = new CampaignMemberPK();
        pk.setOwnerId(userId);
        pk.setCampaignId(campaignUuid);

        // 5. Build and Save the Entity
        CampaignMember member = new CampaignMember();
        member.setId(pk);
        member.setOwner(user.get());
        member.setCampaign(campaign.get());

        // Logic: If the user is the one who created the campaign, they are the MASTER
        if (campaign.get().getCreator().getId().equals(userId)) {
            member.setRole("MASTER");
        } else {
            member.setRole("PLAYER");
        }

        System.out.println("DEBUG: Saving new CampaignMember with role: " + member.getRole());

        CampaignMember savedMember = campaignMemberRepository.save(member);

        campaignMemberRepository.flush();

        return Optional.of(CampaignMemberDetailDTO.fromEntity(savedMember));
    }
}
