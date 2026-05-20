package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgecore.model.entity.*;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignActorService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMemberRepository campaignMemberRepository;
    private final CharacterRepository characterRepository;
    private final CampaignActorRepository campaignActorRepository;
    private final NpcRepository npcRepository;
    private final Random random = new Random();
    private final MapRepository mapRepository;

    public Optional<CampaignActorBootstrapDTO> createCampaignActorCharacter(
            String campaignId, Integer userId, Integer characterId) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Character> character = characterRepository.findById(characterId);
        Optional<CampaignMember> campaignMember = campaignMemberRepository
                .findByIdOwnerIdAndIdCampaignId(userId, UUID.fromString(campaignId));
        Optional<Map> map = mapRepository.findByCampaignId(UUID.fromString(campaignId));

        if (user.isEmpty() || character.isEmpty() || campaignMember.isEmpty() || map.isEmpty()) {
            return Optional.empty();
        }

        CampaignActor campaignActor = new CampaignActor();
        campaignActor.setCharacter(character.get());
        campaignActor.setHp(character.get().getArmor());
        campaignActor.setXp(0);
        campaignActor.setX(random.nextInt(map.get().getWidth()));
        campaignActor.setY(random.nextInt(map.get().getHeight()));
        campaignActor.setWeaponDamage(character.get().getWeaponDamage());
        campaignActor.setType("PLAYER");
        campaignActor.setCampaignMember(campaignMember.get());

        return Optional.of(CampaignActorBootstrapDTO.fromEntity(
                campaignActorRepository.save(campaignActor)));
    }

    public Optional<CampaignActorBootstrapDTO> createCampaignActorNpc(
            String campaignId, Integer userId, Integer npcId) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Npc> npc = npcRepository.findById(npcId);
        Optional<CampaignMember> campaignMember = campaignMemberRepository
                .findByIdOwnerIdAndIdCampaignId(userId, UUID.fromString(campaignId));
        Optional<Map> map = mapRepository.findByCampaignId(UUID.fromString(campaignId));

        if (user.isEmpty() || npc.isEmpty() || campaignMember.isEmpty() || map.isEmpty()) {
            return Optional.empty();
        }

        CampaignActor campaignActor = new CampaignActor();
        campaignActor.setNpc(npc.get());
        campaignActor.setHp(npc.get().getArmor());
        campaignActor.setXp(0);
        campaignActor.setX(random.nextInt(map.get().getWidth()));
        campaignActor.setY(random.nextInt(map.get().getHeight()));
        campaignActor.setWeaponDamage(npc.get().getWeaponDamage());
        campaignActor.setType("NPC");
        campaignActor.setCampaignMember(campaignMember.get());

        return Optional.of(CampaignActorBootstrapDTO.fromEntity(
                campaignActorRepository.save(campaignActor)));
    }

}
