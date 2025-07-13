# Playground Backend

A Spring Boot backend application with FAL.ai image generation integration.

## Features

- **Image Generation**: Generate images using FAL.ai's flux-pro/v1.1-ultra model
- **RESTful API**: Clean REST endpoints with comprehensive OpenAPI documentation
- **Validation**: Input validation with detailed error messages
- **Error Handling**: Comprehensive error handling for API failures
- **Documentation**: Auto-generated OpenAPI/Swagger documentation

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- FAL.ai API key

## Configuration

The application uses the following configuration in `application.properties`:

```properties
# FAL.ai Configuration
fal.api.key=your-fal-api-key-here
fal.api.url=https://api.fal.ai/v1/predictions
fal.model.id=fal-ai/flux-pro/v1.1-ultra
```

## Running the Application

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd playground-backend
   ```

2. **Update the API key** in `src/main/resources/application.properties`:
   ```properties
   fal.api.key=your-actual-fal-api-key
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**:
   - API Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Documentation: `http://localhost:8080/api-docs`

## API Endpoints

### Generate Image

**POST** `/api/generate-image`

Generates images using the FAL.ai flux-pro model.

#### Request Body

```json
{
  "prompt": "Extreme close-up of a single tiger eye, direct frontal view. Detailed iris and pupil. Sharp focus on eye texture and color. Natural lighting to capture authentic eye shine and depth. The word \"FLUX\" is painted over it in big, white brush strokes with visible texture.",
  "num_images": 1,
  "enable_safety_checker": true,
  "output_format": "jpeg",
  "safety_tolerance": "2",
  "aspect_ratio": "16:9"
}
```

#### Parameters

| Parameter | Type | Required | Description | Default |
|-----------|------|----------|-------------|---------|
| `prompt` | String | Yes | Text description for image generation | - |
| `num_images` | Integer | Yes | Number of images to generate (1-4) | - |
| `enable_safety_checker` | Boolean | No | Enable content safety filtering | `true` |
| `output_format` | String | No | Image format (`jpeg` or `png`) | `jpeg` |
| `safety_tolerance` | String | No | Safety level (`1`, `2`, or `3`) | `2` |
| `aspect_ratio` | String | No | Image aspect ratio | `16:9` |

#### Response

```json
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
```

### Health Check

**GET** `/api/generate-image/health`

Returns the health status of the image generation service.

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **400 Bad Request**: Invalid input parameters
- **500 Internal Server Error**: FAL.ai API errors or internal server errors

Error response format:
```json
{
  "status": "error",
  "error": "Detailed error message"
}
```

## Testing

Run the tests with Maven:

```bash
./mvnw test
```

## Development

### Project Structure

```
src/main/java/com/playground/backend/
├── config/
│   ├── FalConfig.java              # FAL.ai configuration
│   └── OpenApiConfig.java          # OpenAPI documentation config
├── controller/
│   ├── HelloController.java        # Basic health endpoints
│   └── ImageGenerationController.java  # Image generation endpoints
├── dto/
│   ├── ImageGenerationRequest.java # Request DTO
│   └── ImageGenerationResponse.java # Response DTO
├── exception/
│   └── GlobalExceptionHandler.java # Global error handling
├── service/
│   └── FalImageGenerationService.java # FAL.ai integration service
└── PlaygroundBackendApplication.java
```

### Adding New Features

1. Create DTOs for request/response
2. Add service layer for business logic
3. Create controller with OpenAPI annotations
4. Add tests
5. Update documentation

## Security Considerations

- API keys are stored in configuration files (use environment variables in production)
- Input validation prevents malicious requests
- Error messages don't expose sensitive information
- Rate limiting should be implemented for production use

## Production Deployment

1. **Environment Variables**: Use environment variables for sensitive configuration
2. **Logging**: Configure appropriate log levels
3. **Monitoring**: Add health checks and metrics
4. **Rate Limiting**: Implement rate limiting for API endpoints
5. **SSL/TLS**: Use HTTPS in production

## License

MIT License - see LICENSE file for details. 