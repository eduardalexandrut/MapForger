package com.example.mapforgecore.service;

import com.example.mapforgecore.model.dto.*;
import com.example.mapforgecore.model.entity.Campaign;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private CharacterService characterService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, CharacterService characterService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.characterService = characterService;
    }

    public AuthResponseDTO signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new AuthResponseDTO(false, "User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        return new AuthResponseDTO(true, "User saved successfully", token, savedUser.getId());
    }

    public AuthResponseDTO signIn(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new AuthResponseDTO(false, "User not found");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return new AuthResponseDTO(false, "Incorrect password");
        }

        String token = getToken(user.get());
        return new AuthResponseDTO(true, "User signed in successfully", token, user.get().getId());
    }

    public Set<UserSummaryDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserSummaryDTO::fromEntity).collect(Collectors.toSet());
    }

    public UserDetailDTO getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Set<CharacterSummaryDTO> characters = user.getCharacters().stream().map(CharacterSummaryDTO::fromEnity).collect(Collectors.toSet());

        //Get all campaigns linked to the user
        Set<CampaignSummaryDTO> campaignsCreated = user.getCampaigns().stream().map(CampaignSummaryDTO::fromEntity).collect(Collectors.toSet());
        Set<CampaignSummaryDTO> campaignsJoined = user.getCharacters().stream()
                .flatMap(c -> characterService.getCampaignsForCharacter(c.getId()).stream())
                .collect(Collectors.toSet());
        Set<CampaignSummaryDTO> allCampaigns = Stream.concat(campaignsCreated.stream(), campaignsJoined.stream()).collect(Collectors.toSet());

        UserDetailDTO userDetailDTO = UserDetailDTO.fromEntity(user, characters, allCampaigns);

        return userDetailDTO;
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

    private String getToken(User user) {
        return jwtService.generateToken(user);
    }
}
