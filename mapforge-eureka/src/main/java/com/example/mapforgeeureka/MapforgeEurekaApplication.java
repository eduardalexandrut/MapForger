package com.example.mapforgeeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MapforgeEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapforgeEurekaApplication.class, args);
    }

}
