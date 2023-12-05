# Task Tracker API Module: Simple Kanban for Individual Productivity

[![Maven build](https://github.com/farneser/task-tracker-api/actions/workflows/maven.yml/badge.svg)](https://github.com/farneser/task-tracker-api/actions/workflows/maven.yml)
[![Docker Image build](https://github.com/farneser/task-tracker-api/actions/workflows/docker.yml/badge.svg)](https://github.com/farneser/task-tracker-api/actions/workflows/docker.yml)

The Task Tracker API module, inspired by the simplicity of a Kanban system, is specifically crafted for individual users
seeking an efficient solution for task management and productivity. This module provides a user-friendly interface
designed to streamline personal workflows and enhance organization.

Use Cases:

* Personal Projects: Effectively manage tasks related to personal projects, hobbies, or side ventures.

* Freelance Work: Stay organized with a dedicated space for tracking freelance tasks, deadlines, and project details.

* Daily Task Management: Enhance daily productivity by visualizing and prioritizing tasks for personal and professional
  growth.

## Authentication

Authentication is performed using JWT with refresh tokens and 256-bit encryption.

During the process of authentication, registration, or token refresh, a valid response will contain a token response
like the following:

```json
{
  "access_token": "",
  "refresh_token": ""
}
```

The output is a JSON object with two fields: `access_token` and `refresh_token`. Authentication occurs only with the
first
token because the `refresh_token` includes the `is_refresh_token` header, preventing its use for authentication. The
refresh
token is exclusively used for updating the main token.

Example authorization header:

```http request
Authorization: Bearer ${access_token}
```

## Endpoints

For detailed information about the available API endpoints, request structures, and response formats, please refer to
the [README.md](src/main/java/dev/farneser/tasktracker/api/web/controllers/README.md). This comprehensive documentation
provides insights into each endpoint's purpose, expected parameters, and the corresponding data returned.

By adhering to JSON for data transmission, our API enhances interoperability and simplifies integration with a variety
of platforms and programming languages.

## Database

In this project, we have Postgres as the database management system to efficiently store and manage our data.
The relational database design is illustrated in the diagram below:

![Database relations](assets/database-relations.svg)

## Build

## Install

```bash
./mvnw clean install
```

## Run tests

```bash
./mvnw clean test
```

## Run

```bash
./mvnw clean spring-boot:run
```

## Run jar

```bash
java -jar target/task-tracker-api-1.0.0.jar
```

## Environment

### Application

| Parameter                         | Default value                               | Description                                                                 |
|-----------------------------------|---------------------------------------------|-----------------------------------------------------------------------------|
| JWT_SECRET                        |                                             | Secret key for JSON Web Token (JWT) authentication                          |
| JWT_EXPIRATION_ACCESS             | `120000`                                    | Expiration time for access JWT tokens (in milliseconds) 2 min by default    |
| JWT_EXPIRATION_REFRESH            | `1209600000`                                | Expiration time for refresh JWT tokens (in milliseconds) 14 days by default |
| LOG_LEVEL                         | `INFO`                                      | Logging level for the application                                           |
| SERVER_PORT                       | `8080`                                      | Port on which the server is running                                         |
| USE_SWAGGER                       | true                                        | Enable/disable Swagger UI for API documentation                             |
| ALLOWED_ORIGINS                   | `http://localhost:3000, http://client:3000` | Comma-separated list of allowed origins for CORS                            |
| EMAIL_CONFIRMATION_REQUIRED       | `true`                                      | Enable/disable email confirmation                                           |
| EMAIL_CONFIRMATION_TOKEN_LIFETIME | `86400000`                                  | Email confirmation code lifetime                                            |

| Parameter         | Default value  | Description                                    |
|-------------------|----------------|------------------------------------------------|
| POSTGRES_HOST     | `localhost`    | PostgreSQL database server IP address          |
| POSTGRES_PORT     | `5432`         | PostgreSQL database server port                |
| POSTGRES_DB       | `task-tracker` | PostgreSQL database name                       |
| POSTGRES_USERNAME | `postgres`     | Username for connecting to PostgreSQL database |
| POSTGRES_PASSWORD | `postgres`     | Password for connecting to PostgreSQL database |

### RabbitMQ

| Parameter         | Default value | Description                         |
|-------------------|---------------|-------------------------------------|
| RABBITMQ_HOST     | `localhost`   | RabbitMQ server host                |
| RABBITMQ_PORT     | `5672`        | RabbitMQ server port                |
| RABBITMQ_USERNAME | `rabbitmq`    | Username for connecting to RabbitMQ |
| RABBITMQ_PASSWORD | `rabbitmq`    | Password for connecting to RabbitMQ |

### Redis

| Parameter  | Default value | Description       |
|------------|---------------|-------------------|
| REDIS_HOST | `locahost`    | Redis server host |
| REDIS_PORT | `6379`        | Redis server port |

## Docker Compose

Example of docker-compose task-tracker-api service:

```yaml
version: '3'

services:
  api:
    image: farneser/task-tracker-api:latest
    container_name: api-container
    environment:
      JWT_SECRET: F40BB648F9CA2303F6878ACD7CF446A28845426C508BF3CBC06740AD892D7B9B # example 256-bit key
      JWT_EXPIRATION_ACCESS: 120000
      JWT_EXPIRATION_REFRESH: 1209600000
      LOG_LEVEL: INFO
      SERVER_PORT: 8080
      USE_SWAGGER: true
      ALLOWED_ORIGINS: http://localhost:3000, http://client:3000
      POSTGRES_HOST: localhost
      POSTGRES_PORT: 5432
      POSTGRES_DB: task-tracker
      POSTGRES_USERNAME: postgres
      POSTGRES_PASSWORD: postgres
      RABBITMQ_HOST: localhost
      RABBITMQ_PORT: 5672
      RABBITMQ_USERNAME: rabbitmq
      RABBITMQ_PASSWORD: rabbitmq
      REDIS_HOST: localhost
      REDIS_PORT: 6379
    ports:
      - "8080:8080"
```