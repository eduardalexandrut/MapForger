package com.example.mapforgecore.controller;

import com.example.mapforgecore.model.dto.UserDetailDTO;
import com.example.mapforgecore.model.dto.UserSummaryDTO;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.UserRepository;
import com.example.mapforgecore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/v1/users -> all users (summary)
    @GetMapping
    public Set<UserSummaryDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /api/v1/users/{id} -> single user by id
    @GetMapping("/{id}")
    public UserDetailDTO getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }


}
