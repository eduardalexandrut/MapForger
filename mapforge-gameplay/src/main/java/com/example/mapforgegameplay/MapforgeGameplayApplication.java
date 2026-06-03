package com.example.mapforgegameplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class MapforgeGameplayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MapforgeGameplayApplication.class);

        // Load .env
        var dotenv = io.github.cdimascio.dotenv.Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Add .env values as Spring property source
        app.addInitializers(applicationContext -> {
            var env = (org.springframework.core.env.ConfigurableEnvironment) applicationContext.getEnvironment();

            var map = new java.util.HashMap<String, Object>();
            dotenv.entries().forEach(entry -> map.put(entry.getKey(), entry.getValue()));

            var propertySource = new org.springframework.core.env.MapPropertySource("dotenvProps", map);
            env.getPropertySources().addFirst(propertySource);
        });

        app.run(args);
    }

}
