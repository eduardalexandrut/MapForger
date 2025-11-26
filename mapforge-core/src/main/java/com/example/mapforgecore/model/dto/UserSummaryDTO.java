package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.service.UserService;

public record UserSummaryDTO(
        Integer id,
        String username
) {
    public static UserSummaryDTO fromEntity(User user) {
        return new UserSummaryDTO(user.getId(), user.getUsername());
    }
}
