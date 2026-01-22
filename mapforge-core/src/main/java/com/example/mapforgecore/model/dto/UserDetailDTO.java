package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDetailDTO(
        Integer id,
        String username,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String email,
        String  pic,
        LocalDate joinedDate,
        Set<CharacterSummaryDTO> characters
      //  Set<CampaignSummaryDTO> campaigns
) {
    public static UserDetailDTO fromEntity(User user) {
        Set<CharacterSummaryDTO> characters = user.getCharacters().stream()
                .map(ce -> new CharacterSummaryDTO(
                        ce.getId(),
                        ce.getName(),
                        ce.getRace(),
                        ce.getAlignment(),
                        ce.getArmor(),
                        ce.getWeaponDamage(),
                        ce.getSpeed(),
                        ce.getDescription(),
                        ce.getBackstory(),
                        ce.getPic(),
                        ce.getCreatedAt(),
                        new UserSummaryDTO(user.getId(), user.getUsername(), user.getPic(), user.getJoinedDate())
                )).collect(Collectors.toSet());

        return new UserDetailDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getPic(),
                user.getJoinedDate(),
                characters
        );
    }
}
