package com.example.mapforgecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapforgeCoreApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MapforgeCoreApplication.class);

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
