package com.playground.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.playground.backend.config.FalConfig;

@SpringBootApplication
@EnableConfigurationProperties(FalConfig.class)
public class PlaygroundBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundBackendApplication.class, args);
    }
} 