package com.example.mapforgecore.service;

import com.example.mapforgecore.constants.DefaultNpcs;
import com.example.mapforgecore.model.dto.*;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.Map;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMemberRepository campaignMemberRepository;
    private final MapRepository mapRepository;
    private final CampaignMemberService campaignMemberService;
    private final CampaignActorService campaignActorService;
    private final GameplayClient gameplayClient;
    private final CharacterRepository characterRepository;
    private final CampaignActorRepository campaignActorRepository;

    public CampaignService(CampaignRepository campaignRepository,
                           UserRepository userRepository,
                           CampaignMemberRepository campaignMemberRepository,
                           MapRepository mapRepository,
                           CampaignMemberService campaignMemberService,
                           CampaignActorService campaignActorService,
                           GameplayClient gameplayClient,
                           CampaignActorRepository campaignActorRepository,
                           CharacterRepository  characterRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
        this.campaignMemberRepository = campaignMemberRepository;
        this.mapRepository = mapRepository;
        this.campaignMemberService = campaignMemberService;
        this.campaignActorService = campaignActorService;
        this.gameplayClient = gameplayClient;
        this.campaignActorRepository = campaignActorRepository;
        this.characterRepository = characterRepository;
    }


    public CampaignSummaryDTO createCampaign(CampaignFormDTO campaignFormDTO) {
        User creator = userRepository.findById(campaignFormDTO.creatorId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Map map = mapRepository.findById(campaignFormDTO.mapId())
                .orElseThrow(() -> new EntityNotFoundException("Map not found"));

        Campaign campaign = new Campaign(campaignFormDTO.name(), campaignFormDTO.description(), creator, map, campaignFormDTO.pic());

        Campaign saved = campaignRepository.save(campaign);

        //Create default CampaignMember for Npcs
        campaignMemberService.createCampaignMember(String.valueOf(saved.getId()), creator.getId(), DefaultNpcs.HUMANOID.getId());
        campaignMemberService.createCampaignMember(String.valueOf(saved.getId()), creator.getId(), DefaultNpcs.BEAST.getId());
        campaignMemberService.createCampaignMember(String.valueOf(saved.getId()), creator.getId(), DefaultNpcs.UNDEAD.getId());

        //Create default CampaignActor for Npcs
        campaignActorService.createCampaignActorNpc(String.valueOf(saved.getId()), creator.getId(), DefaultNpcs.HUMANOID.getId());
        campaignActorService.createCampaignActorNpc(String.valueOf(saved.getId()), creator.getId(), DefaultNpcs.BEAST.getId());
        campaignActorService.createCampaignActorNpc(String.valueOf(saved.getId()), creator.getId(), DefaultNpcs.UNDEAD.getId());

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
                .createCampaignActorCharacter(campaignId, userId, characterId);
    }

    public boolean isCampaignMember(String id, Integer userId) {
        return campaignMemberRepository.findByIdOwnerIdAndIdCampaignId(userId, UUID.fromString(id)).isPresent();
    }

    public CampaignHistoryDisplayDTO getCampaignHistory(String campaignId) {
        // 1. Fetch raw history from Gameplay via Feign
        CampaignHistoryDTO raw = gameplayClient.getHistory(UUID.fromString(campaignId));

        // 2. Fetch all actors for this campaign to build id → name map
        List<CampaignActorBootstrapDTO> actors = campaignActorRepository
                .findByCampaignId(UUID.fromString(campaignId))
                .stream()
                .map(CampaignActorBootstrapDTO::fromEntity)
                .toList();

        // 3. Build actorId → character name map
        java.util.Map<Integer, String> actorNames = new HashMap<>();
        actors.forEach(actor -> {
            if (actor.getCharacterId() != null) {
                characterRepository.findById(Integer.valueOf(String.valueOf(actor.getCharacterId())))
                        .ifPresent(c -> actorNames.put(actor.getId(), c.getName()));
            }
        });

        // 4. Enrich turns
        List<TurnHistoryDisplayDTO> turns = raw.getTurns().stream()
                .map(turn -> new TurnHistoryDisplayDTO(
                        turn.getIndex(),
                        actorNames.getOrDefault(turn.getActorId(), "Actor #" + turn.getActorId()),
                        turn.getCompleted(),
                        turn.getActions().stream()
                                .map(action -> new ActionHistoryDisplayDTO(
                                        action.getType(),
                                        actorNames.getOrDefault(action.getActorId(), "Actor #" + action.getActorId()),
                                        actorNames.getOrDefault(action.getTargetId(), action.getTargetId() != null ? "Actor #" + action.getTargetId() : null),
                                        action.getX(), action.getY(),
                                        action.getDamage(),
                                        actorNames.getOrDefault(action.getKillerId(), null),
                                        action.getCretedAt()
                                )).toList()
                )).toList();

        return new CampaignHistoryDisplayDTO(
                UUID.fromString(campaignId),
                raw.getStatus(),
                raw.getStartedAt(),
                raw.getFinishedAt(),
                raw.getTotalTurns(),
                turns
        );
    }
}
