package com.playground.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Playground Backend API")
                        .description("Spring Boot backend API with FAL.ai image generation integration. " +
                                   "This API provides endpoints for generating images using the FAL.ai flux-pro model.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Playground Backend")
                                .email("support@playground.com")
                                .url("https://github.com/playground-backend"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server"),
                        new Server()
                                .url("https://api.playground.com")
                                .description("Production server")
                ));
    }
} 