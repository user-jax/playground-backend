# Playground Backend

A Spring Boot backend application for the playground project.

## Features

- Spring Boot 3.2.0
- Spring Data JPA
- H2 in-memory database
- RESTful API endpoints
- Maven build system

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### Running the Application

1. Navigate to the project directory:
   ```bash
   cd playground-backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Available Endpoints

- `GET /api/hello` - Returns a hello message
- `GET /api/health` - Health check endpoint
- `GET /h2-console` - H2 database console (for development)

### Database

The application uses H2 in-memory database for development:
- URL: `jdbc:h2:mem:playgrounddb`
- Username: `sa`
- Password: `password`
- Console: `http://localhost:8080/h2-console`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/playground/backend/
│   │       ├── PlaygroundBackendApplication.java
│   │       └── controller/
│   │           └── HelloController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/playground/backend/
```

## Development

To add new features:
1. Create controllers in the `controller` package
2. Add entities in a new `entity` package
3. Add repositories in a new `repository` package
4. Add services in a new `service` package 