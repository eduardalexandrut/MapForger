package com.example.mapforgegameplay.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockMessagingConfig {

    @Bean
    @Primary
    public SimpMessageSendingOperations messagingTemplate() {
        return mock(SimpMessageSendingOperations.class);
    }
}