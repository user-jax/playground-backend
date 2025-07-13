package com.playground.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.backend.dto.ImageGenerationRequest;
import com.playground.backend.dto.ImageGenerationResponse;
import com.playground.backend.service.FalImageGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ImageGenerationController.class)
public class ImageGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FalImageGenerationService imageGenerationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGenerateImage_Success() throws Exception {
        // Given
        ImageGenerationRequest request = new ImageGenerationRequest();
        request.setPrompt("A beautiful sunset");
        request.setNumImages(1);

        ImageGenerationResponse.GeneratedImage generatedImage = 
            new ImageGenerationResponse.GeneratedImage(
                "https://fal.media/files/test-image.jpg", 
                1920, 
                1080, 
                "image/jpeg"
            );

        ImageGenerationResponse response = new ImageGenerationResponse(
            List.of(generatedImage), 
            new Object(), 
            1234567890L, 
            List.of(false), 
            "A beautiful sunset"
        );

        when(imageGenerationService.generateImage(any(ImageGenerationRequest.class)))
            .thenReturn(Mono.just(response));

        // When & Then
        mockMvc.perform(post("/api/generate-image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.images[0].url").value("https://fal.media/files/test-image.jpg"))
                .andExpect(jsonPath("$.images[0].width").value(1920))
                .andExpect(jsonPath("$.images[0].height").value(1080));
    }

    @Test
    public void testGenerateImage_ValidationError() throws Exception {
        // Given
        ImageGenerationRequest request = new ImageGenerationRequest();
        request.setPrompt(""); // Empty prompt should fail validation
        request.setNumImages(0); // Invalid number of images

        // When & Then
        mockMvc.perform(post("/api/generate-image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGenerateImage_ServiceError() throws Exception {
        // Given
        ImageGenerationRequest request = new ImageGenerationRequest();
        request.setPrompt("A beautiful sunset");
        request.setNumImages(1);

        ImageGenerationResponse errorResponse = new ImageGenerationResponse("FAL.ai API error");

        when(imageGenerationService.generateImage(any(ImageGenerationRequest.class)))
            .thenReturn(Mono.just(errorResponse));

        // When & Then
        mockMvc.perform(post("/api/generate-image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.error").value("FAL.ai API error"));
    }
} 