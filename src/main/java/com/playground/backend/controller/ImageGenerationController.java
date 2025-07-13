package com.playground.backend.controller;

import com.playground.backend.dto.ImageGenerationRequest;
import com.playground.backend.dto.ImageGenerationResponse;
import com.playground.backend.service.FalImageGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Image Generation", description = "APIs for generating images using FAL.ai flux-pro model")
public class ImageGenerationController {

    private static final Logger logger = LoggerFactory.getLogger(ImageGenerationController.class);

    private final FalImageGenerationService imageGenerationService;

    @Autowired
    public ImageGenerationController(FalImageGenerationService imageGenerationService) {
        this.imageGenerationService = imageGenerationService;
    }

    @PostMapping("/generate-image")
    @Operation(
        summary = "Generate images using FAL.ai flux-pro model",
        description = "Generates images based on a text prompt using the FAL.ai flux-pro/v1.1-ultra model. " +
                    "Supports various parameters like number of images, aspect ratio, safety settings, and output format.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Image generation request parameters",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImageGenerationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Basic Tiger Eye",
                        summary = "Generate a single tiger eye image",
                        value = """
                        {
                          "prompt": "Extreme close-up of a single tiger eye, direct frontal view. Detailed iris and pupil. Sharp focus on eye texture and color. Natural lighting to capture authentic eye shine and depth. The word \"FLUX\" is painted over it in big, white brush strokes with visible texture.",
                          "num_images": 1,
                          "enable_safety_checker": true,
                          "output_format": "jpeg",
                          "safety_tolerance": "2",
                          "aspect_ratio": "16:9"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Multiple Images",
                        summary = "Generate multiple landscape images",
                        value = """
                        {
                          "prompt": "A serene mountain landscape at sunset with golden light filtering through clouds",
                          "num_images": 3,
                          "enable_safety_checker": true,
                          "output_format": "jpeg",
                          "safety_tolerance": "2",
                          "aspect_ratio": "16:9"
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Images generated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImageGenerationResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        summary = "Successful image generation",
                        value = """
                        {
                          "images": [
                            {
                              "url": "https://fal.media/files/panda/0p6XD090UqfnRLH8BZwj9_e9edcb84b09e43e89eeef3d5a6df92f0.jpg",
                              "width": 2752,
                              "height": 1536,
                              "content_type": "image/jpeg"
                            }
                          ],
                          "timings": {},
                          "seed": 1627638640,
                          "has_nsfw_concepts": [false],
                          "prompt": "Extreme close-up of a single tiger eye...",
                          "status": "success"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImageGenerationResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Validation Error",
                        summary = "Invalid input parameters",
                        value = """
                        {
                          "status": "error",
                          "error": "Number of images must be at least 1"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error or FAL.ai API error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ImageGenerationResponse.class),
                examples = {
                    @ExampleObject(
                        name = "API Error",
                        summary = "FAL.ai API error",
                        value = """
                        {
                          "status": "error",
                          "error": "FAL.ai API error (HTTP 429): Rate limit exceeded"
                        }
                        """
                    )
                }
            )
        )
    })
    public Mono<ResponseEntity<ImageGenerationResponse>> generateImage(
            @Parameter(description = "Image generation request parameters", required = true)
            @Valid @RequestBody ImageGenerationRequest request) {

        logger.info("Received image generation request with prompt: {}", request.getPrompt());

        return imageGenerationService.generateImage(request)
                .map(response -> {
                    if ("error".equals(response.getStatus())) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(error -> {
                    logger.error("Unexpected error in image generation: {}", error.getMessage(), error);
                    ImageGenerationResponse errorResponse = new ImageGenerationResponse(
                            "Unexpected error: " + error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    @GetMapping("/generate-image/health")
    @Operation(
        summary = "Check image generation service health",
        description = "Simple health check endpoint for the image generation service"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service is healthy",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Health Check",
                    summary = "Service health status",
                    value = """
                    {
                      "status": "UP",
                      "service": "image-generation",
                      "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "image-generation",
                "timestamp", java.time.LocalDateTime.now()
        ));
    }
} 