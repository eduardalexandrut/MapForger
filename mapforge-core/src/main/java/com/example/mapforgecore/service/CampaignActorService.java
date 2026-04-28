package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgecore.model.entity.CampaignActor;
import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignActorService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMemberRepository campaignMemberRepository;
    private final CharacterRepository characterRepository;
    private final CampaignActorRepository campaignActorRepository;

    public Optional<CampaignActorBootstrapDTO> createCampaignActor(
            String campaignId, Integer userId, Integer characterId) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Character> character = characterRepository.findById(characterId);
        Optional<CampaignMember> campaignMember = campaignMemberRepository
                .findByIdOwnerIdAndIdCampaignId(userId, UUID.fromString(campaignId));

        if (user.isEmpty() || character.isEmpty() || campaignMember.isEmpty()) {
            return Optional.empty();
        }

        CampaignActor campaignActor = new CampaignActor();
        campaignActor.setCharacter(character.get());
        campaignActor.setHp(character.get().getArmor());
        campaignActor.setXp(0);
        campaignActor.setX(0);//FIXME
        campaignActor.setY(0);//FIXME
        campaignActor.setWeaponDamage(character.get().getWeaponDamage());
        campaignActor.setType("PLAYER");
        campaignActor.setCampaignMember(campaignMember.get());

        return Optional.of(CampaignActorBootstrapDTO.fromEntity(
                campaignActorRepository.save(campaignActor)));
    }

}
