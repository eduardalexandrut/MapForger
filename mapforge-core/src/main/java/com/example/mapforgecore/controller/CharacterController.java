package com.example.mapforgecore.controller;

import com.example.mapforgecore.model.dto.CharacterDetailDTO;
import com.example.mapforgecore.model.dto.CharacterSummaryDTO;
import com.example.mapforgecore.model.entity.Character;
import com.example.mapforgecore.model.entity.User;
import com.example.mapforgecore.repository.CharacterRepository;
import com.example.mapforgecore.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    // GET /api/characters → all characters
    @GetMapping
    public Set<CharacterSummaryDTO> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    // GET /api/characters/{id} → single characters by id
    @GetMapping("/{id}")
    public CharacterDetailDTO getCharacterById(@PathVariable Integer id) {
        return characterService.getCharacterById(id);
    }

    // POST /api/characters -> create a character
    @PostMapping
    public ResponseEntity<CharacterSummaryDTO> createCharacter(@RequestBody CharacterSummaryDTO character) {
        return characterService.createCharacter(character)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/v1/characters/{id} -> update a character
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDetailDTO> updateCharacter(@PathVariable Integer id, @RequestBody Character character) {
        return characterService.updateCharacter(id, character)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/characters/{id} -> delete a character
    @DeleteMapping("/{id}")
    public ResponseEntity<CharacterSummaryDTO> deleteCharacter(@PathVariable Integer id) {
        return characterService.deleteCharacter(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
