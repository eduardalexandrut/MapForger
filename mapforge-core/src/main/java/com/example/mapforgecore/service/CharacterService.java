package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.CampaignSummaryDTO;
import com.example.mapforgecore.model.dto.CharacterDetailDTO;
import com.example.mapforgecore.model.dto.CharacterSummaryDTO;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.CampaignActor;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.CampaignActorRepository;
import com.example.mapforgecore.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CampaignActorRepository campaignActorRepository;

    public CharacterService(CharacterRepository characterRepository, CampaignActorRepository campaignActorRepository) {
        this.characterRepository = characterRepository;
        this.campaignActorRepository = campaignActorRepository;
    }

    public Set<CharacterSummaryDTO> getAllCharacters() {
        return this.characterRepository.findAll().stream().map(CharacterSummaryDTO::fromEnity).collect(Collectors.toSet());
    }

    public CharacterDetailDTO getCharacterById(Integer id) {
        Set<CampaignSummaryDTO> campaigns = getCampaignsForCharacter(id);
        return this.characterRepository.findById(id).map(character -> CharacterDetailDTO.fromEnity(character, campaigns)).orElse(null);
    }

    public Optional<CharacterDetailDTO> updateCharacter(Integer id, Character updatedCharacter) {
        Optional<Character> oldCharacter = characterRepository.findById(id);
        Set<CampaignSummaryDTO> campaigns = getCampaignsForCharacter(id);

        if (oldCharacter.isEmpty()) {
            return Optional.empty();
        }

        oldCharacter.get().setName(
                Optional.ofNullable(updatedCharacter.getName()).orElse(oldCharacter.get().getName())
        );
        oldCharacter.get().setArmor(
                Optional.ofNullable(updatedCharacter.getArmor()).orElse(oldCharacter.get().getArmor())
        );
        oldCharacter.get().setSpeed(
                Optional.ofNullable(updatedCharacter.getSpeed()).orElse(oldCharacter.get().getSpeed())
        );
        oldCharacter.get().setWeaponDamage(
                Optional.ofNullable(updatedCharacter.getWeaponDamage()).orElse(oldCharacter.get().getWeaponDamage())
        );
        oldCharacter.get().setAlignment(
                Optional.ofNullable(updatedCharacter.getAlignment()).orElse(oldCharacter.get().getAlignment())
        );
        oldCharacter.get().setRace(
                Optional.ofNullable(updatedCharacter.getRace()).orElse(oldCharacter.get().getRace())
        );

        return Optional.of(CharacterDetailDTO.fromEnity(characterRepository.save(oldCharacter.get()), campaigns));
    }

    //FIXME set value null to fks
    public Optional<CharacterSummaryDTO> deleteCharacter(Integer id) {
        Optional<Character> character = characterRepository.findById(id);

        if (character.isEmpty()) {
            return Optional.empty();
        }

        characterRepository.deleteById(id);
        return Optional.of(CharacterSummaryDTO.fromEnity(character.get()));
    }

    public Set<CampaignSummaryDTO> getCampaignsForCharacter(Integer characterId) {
        return campaignActorRepository.findCampaignsByCharacterId(characterId)
                .stream()
                .map(CampaignSummaryDTO::fromEntity)
                .collect(Collectors.toSet());
    }

    public Optional<CharacterSummaryDTO> createCharacter(CharacterSummaryDTO characterSummaryDTO) {
        Character character = CharacterSummaryDTO.fromDTO(characterSummaryDTO);
        return Optional.of(CharacterSummaryDTO.fromEnity(characterRepository.save(character)));
    }
}
