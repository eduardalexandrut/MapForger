package com.example.mapforgegameplay.mock;

import com.example.mapforgegameplay.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgegameplay.service.CoreClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

@TestConfiguration
public class MockCoreClientConfig {
    @Bean
    @Primary
    public CoreClient coreCl() {
        return campaignId -> List.of(
                new CampaignActorBootstrapDTO(1, "CHARACTER", 100, 0, 15, 1, campaignId),
                new CampaignActorBootstrapDTO(2, "CHARACTER", 80, 0, 20, 2, campaignId),
                new CampaignActorBootstrapDTO(3, "CHARACTER", 60, 0, 10, 3, campaignId)
        );
    }
}
