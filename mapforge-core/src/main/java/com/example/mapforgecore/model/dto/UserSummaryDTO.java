package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.service.UserService;

import java.time.LocalDate;

public record UserSummaryDTO(
        Integer id,
        String username,
        String  pic,
        LocalDate joinedDate
) {
    public static UserSummaryDTO fromEntity(User user) {
        return new UserSummaryDTO(user.getId(), user.getUsername(), user.getPic(), user.getJoinedDate());
    }
}
