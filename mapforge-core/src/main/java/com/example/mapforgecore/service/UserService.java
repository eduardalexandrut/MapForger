package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.AuthResponseDTO;
import com.example.mapforgecore.model.dto.UserDetailDTO;
import com.example.mapforgecore.model.dto.UserSummaryDTO;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //TODO password checks (length, strength, etc...)
    public AuthResponseDTO signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new AuthResponseDTO(false, "User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new AuthResponseDTO(true, "User saved successfully", savedUser);
    }

    public AuthResponseDTO signIn(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new AuthResponseDTO(false, "User not found");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return new AuthResponseDTO(false, "Incorrect password");
        }

        String token = jwtService.generateToken(user.get());
        return new AuthResponseDTO(true, "User signed in successfully", token);
    }

    public Set<UserSummaryDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserSummaryDTO::fromEntity).collect(Collectors.toSet());
    }

    public UserDetailDTO getUserById(Integer id) {
        return userRepository.findById(id).map(UserDetailDTO::fromEntity).orElse(null);
    }

    public Optional<User> updateUser(Integer id, User updatedUser) {
        Optional<User> oldUser = userRepository.findById(id);

        if (oldUser.isEmpty()) {
            return Optional.empty();
        }

        //Only certain fields are updatable
        oldUser.get().setUsername(
                Optional.ofNullable(updatedUser.getUsername()).orElse(oldUser.get().getUsername())
        );
        oldUser.get().setName(
                Optional.ofNullable(updatedUser.getName()).orElse(oldUser.get().getName())
        );
        oldUser.get().setSurname(
                Optional.ofNullable(updatedUser.getSurname()).orElse(oldUser.get().getSurname())
        );
        oldUser.get().setDateOfBirth(
                Optional.ofNullable(updatedUser.getDateOfBirth()).orElse(oldUser.get().getDateOfBirth())
        );
        oldUser.get().setEmail(
                Optional.ofNullable(updatedUser.getEmail()).orElse(oldUser.get().getEmail())
        );

        //TODO password checks (length, strength, etc...)
        if (Optional.ofNullable(updatedUser.getPassword()).isPresent() && !updatedUser.getPassword().isEmpty()) {
            oldUser.get().setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return Optional.of(userRepository.save(oldUser.get()));
    }

    //FIXME set value null to fks
    public Optional<User> deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        userRepository.deleteById(id);
        return user;
    }
}
