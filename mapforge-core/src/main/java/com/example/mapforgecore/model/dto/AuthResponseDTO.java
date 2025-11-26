package com.example.mapforgecore.model.dto;

import com.example.mapforgecore.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String message;
    private boolean success;
    private User user;
    private String token;

    public AuthResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponseDTO(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public AuthResponseDTO(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
