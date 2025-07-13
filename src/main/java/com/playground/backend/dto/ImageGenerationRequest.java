package com.playground.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Schema(description = "Request for image generation using FAL.ai flux-pro model")
public class ImageGenerationRequest {

    @Schema(description = "Text prompt for image generation", example = "Extreme close-up of a single tiger eye, direct frontal view. Detailed iris and pupil.")
    @NotBlank(message = "Prompt is required")
    private String prompt;

    @Schema(description = "Number of images to generate", example = "1", minimum = "1", maximum = "4")
    @NotNull(message = "Number of images is required")
    @Min(value = 1, message = "Number of images must be at least 1")
    @Max(value = 4, message = "Number of images cannot exceed 4")
    @JsonProperty("num_images")
    private Integer numImages;

    @Schema(description = "Enable safety checker for content filtering", example = "true")
    @JsonProperty("enable_safety_checker")
    private Boolean enableSafetyChecker = true;

    @Schema(description = "Output format for generated images", example = "jpeg", allowableValues = {"jpeg", "png"})
    @JsonProperty("output_format")
    private String outputFormat = "jpeg";

    @Schema(description = "Safety tolerance level", example = "2", allowableValues = {"1", "2", "3"})
    @JsonProperty("safety_tolerance")
    private String safetyTolerance = "2";

    @Schema(description = "Aspect ratio for generated images", example = "16:9", allowableValues = {"1:1", "16:9", "9:16", "4:3", "3:4"})
    @JsonProperty("aspect_ratio")
    private String aspectRatio = "16:9";

    // Default constructor
    public ImageGenerationRequest() {}

    // Constructor with required fields
    public ImageGenerationRequest(String prompt, Integer numImages) {
        this.prompt = prompt;
        this.numImages = numImages;
    }

    // Getters and Setters
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getNumImages() {
        return numImages;
    }

    public void setNumImages(Integer numImages) {
        this.numImages = numImages;
    }

    public Boolean getEnableSafetyChecker() {
        return enableSafetyChecker;
    }

    public void setEnableSafetyChecker(Boolean enableSafetyChecker) {
        this.enableSafetyChecker = enableSafetyChecker;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getSafetyTolerance() {
        return safetyTolerance;
    }

    public void setSafetyTolerance(String safetyTolerance) {
        this.safetyTolerance = safetyTolerance;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
} 