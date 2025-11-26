package com.example.mapforgecore.repository;

import com.example.mapforgecore.model.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    default Optional<Campaign> findById(String id) {
        try {
            return findById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    default boolean existsById(String id) {
        try {
            return existsById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    default void deleteById(String id) {
        try {
            deleteById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw  new IllegalArgumentException("Campaign id " + id + " does not exist");
        }
    }
}
