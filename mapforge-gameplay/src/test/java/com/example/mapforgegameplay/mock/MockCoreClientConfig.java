package com.example.mapforgegameplay.mock;

import com.example.mapforgegameplay.model.dto.CampaignActorBootstrapDTO;
import com.example.mapforgegameplay.model.entity.Map;
import com.example.mapforgegameplay.service.CoreClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.UUID;

@TestConfiguration
public class MockCoreClientConfig {
    @Bean(name = "com.example.mapforgegameplay.service.CoreClient")
    @Primary
    public CoreClient coreClient() {
        return new CoreClient() {
            @Override
            public List<CampaignActorBootstrapDTO> getActorsForCampaign(UUID campaignId) {
                return List.of(/* your mock actors */);
            }

            @Override
            public Map getMapByCampaignId(UUID campaignId) {
                // Return a mock map for your tests
                System.out.println("DEBUG: Mock getMapByCampaignId called for: " + campaignId);
                Map mockMap = new Map(
                        1,                          // id
                        "Standard Battle Arena",    // name
                        "A basic 20x20 testing map",// description
                        20,                         // width
                        20,                         // height
                        "forest_theme.png",         // pic
                        null                        // createdAt (null because it's DB-generated)
                );
                return mockMap;
            }
        };
    }
}
