package com.playground.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response from image generation API")
public class ImageGenerationResponse {

    @Schema(description = "List of generated images with URLs and metadata")
    private List<GeneratedImage> images;

    @Schema(description = "Timing information for the generation process")
    private Object timings;

    @Schema(description = "Seed used for image generation")
    private Long seed;

    @Schema(description = "NSFW content flags for each generated image")
    @JsonProperty("has_nsfw_concepts")
    private List<Boolean> hasNsfwConcepts;

    @Schema(description = "Original prompt used for generation")
    private String prompt;

    @Schema(description = "Status of the generation request")
    private String status;

    @Schema(description = "Error message if generation failed")
    private String error;

    // Default constructor
    public ImageGenerationResponse() {}

    // Constructor for successful response
    public ImageGenerationResponse(List<GeneratedImage> images, Object timings, Long seed, 
                                 List<Boolean> hasNsfwConcepts, String prompt) {
        this.images = images;
        this.timings = timings;
        this.seed = seed;
        this.hasNsfwConcepts = hasNsfwConcepts;
        this.prompt = prompt;
        this.status = "success";
    }

    // Constructor for error response
    public ImageGenerationResponse(String error) {
        this.error = error;
        this.status = "error";
    }

    // Getters and Setters
    public List<GeneratedImage> getImages() {
        return images;
    }

    public void setImages(List<GeneratedImage> images) {
        this.images = images;
    }

    public Object getTimings() {
        return timings;
    }

    public void setTimings(Object timings) {
        this.timings = timings;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public List<Boolean> getHasNsfwConcepts() {
        return hasNsfwConcepts;
    }

    public void setHasNsfwConcepts(List<Boolean> hasNsfwConcepts) {
        this.hasNsfwConcepts = hasNsfwConcepts;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Schema(description = "Generated image with URL and metadata")
    public static class GeneratedImage {
        @Schema(description = "URL of the generated image")
        private String url;

        @Schema(description = "Width of the generated image in pixels")
        private Integer width;

        @Schema(description = "Height of the generated image in pixels")
        private Integer height;

        @Schema(description = "Content type of the image")
        @JsonProperty("content_type")
        private String contentType;

        // Default constructor
        public GeneratedImage() {}

        // Constructor with all fields
        public GeneratedImage(String url, Integer width, Integer height, String contentType) {
            this.url = url;
            this.width = width;
            this.height = height;
            this.contentType = contentType;
        }

        // Getters and Setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }
} 