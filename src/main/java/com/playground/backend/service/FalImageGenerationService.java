package com.playground.backend.service;

import com.playground.backend.config.FalConfig;
import com.playground.backend.dto.ImageGenerationRequest;
import com.playground.backend.dto.ImageGenerationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class FalImageGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(FalImageGenerationService.class);

    private final WebClient webClient;
    private final FalConfig falConfig;

    @Autowired
    public FalImageGenerationService(FalConfig falConfig) {
        this.falConfig = falConfig;
        
        // Debug logging
        logger.info("FAL Config - API URL: {}", falConfig.getApiUrl());
        logger.info("FAL Config - Model ID: {}", falConfig.getModelId());
        logger.info("FAL Config - API Key: {}", falConfig.getApiKey() != null ? "***" + falConfig.getApiKey().substring(falConfig.getApiKey().length() - 4) : "NULL");
        
        if (falConfig.getApiUrl() == null || falConfig.getApiUrl().isEmpty()) {
            throw new IllegalStateException("FAL API URL is not configured");
        }
        
        this.webClient = WebClient.builder()
                .baseUrl(falConfig.getApiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Key " + falConfig.getApiKey())
                .build();
    }

    public Mono<ImageGenerationResponse> generateImage(ImageGenerationRequest request) {
        logger.info("Generating image with prompt: {}", request.getPrompt());

        // Prepare the request payload for FAL.ai
        Map<String, Object> falRequest = new HashMap<>();
        falRequest.put("prompt", request.getPrompt());
        falRequest.put("num_images", request.getNumImages());
        falRequest.put("enable_safety_checker", request.getEnableSafetyChecker());
        falRequest.put("output_format", request.getOutputFormat());
        falRequest.put("safety_tolerance", request.getSafetyTolerance());
        falRequest.put("aspect_ratio", request.getAspectRatio());

        return webClient.post()
                .uri("/" + falConfig.getModelId())
                .bodyValue(falRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapFalResponseToImageGenerationResponse)
                .onErrorResume(this::handleError);
    }

    private ImageGenerationResponse mapFalResponseToImageGenerationResponse(Map<String, Object> falResponse) {
        try {
            // FAL.ai response has data at root level, not nested under "output"
            // Extract images from the response
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> imagesData = (java.util.List<Map<String, Object>>) falResponse.get("images");
            java.util.List<ImageGenerationResponse.GeneratedImage> images = new java.util.ArrayList<>();

            if (imagesData != null) {
                for (Map<String, Object> imageData : imagesData) {
                    ImageGenerationResponse.GeneratedImage image = new ImageGenerationResponse.GeneratedImage(
                            (String) imageData.get("url"),
                            (Integer) imageData.get("width"),
                            (Integer) imageData.get("height"),
                            (String) imageData.get("content_type")
                    );
                    images.add(image);
                }
            }

            // Extract other fields from root level
            Object timings = falResponse.get("timings");
            Long seed = falResponse.get("seed") != null ? Long.valueOf(falResponse.get("seed").toString()) : null;
            @SuppressWarnings("unchecked")
            java.util.List<Boolean> hasNsfwConcepts = (java.util.List<Boolean>) falResponse.get("has_nsfw_concepts");
            String prompt = (String) falResponse.get("prompt");

            return new ImageGenerationResponse(images, timings, seed, hasNsfwConcepts, prompt);

        } catch (Exception e) {
            logger.error("Error mapping FAL response: {}", e.getMessage(), e);
            return new ImageGenerationResponse("Error processing FAL.ai response: " + e.getMessage());
        }
    }

    private Mono<ImageGenerationResponse> handleError(Throwable error) {
        logger.error("Error calling FAL.ai API: {}", error.getMessage(), error);

        if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            String errorMessage = String.format("FAL.ai API error (HTTP %d): %s", 
                    wcre.getStatusCode().value(), wcre.getResponseBodyAsString());
            return Mono.just(new ImageGenerationResponse(errorMessage));
        }

        return Mono.just(new ImageGenerationResponse("Error calling FAL.ai API: " + error.getMessage()));
    }
} 