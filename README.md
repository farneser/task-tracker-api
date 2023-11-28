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

```bash
./mvnw clean install
```

## Environment

| Parameter              | Default value                               | Description                                 |
|------------------------|---------------------------------------------|---------------------------------------------|
| POSTGRES_HOST          | `localhost`                                 | IP address of the Postgres DB server        |
| POSTGRES_PORT          | `5432`                                      | Port of the Postgres DB server              |
| POSTGRES_DB            | `task-tracker`                              | Postgres database name                      |
| POSTGRES_USERNAME      | `postgres`                                  | Postgres username                           |
| POSTGRES_PASSWORD      | `postgres`                                  | Postgres password                           |
| JWT_SECRET             |                                             | JSON Web Token secret encoder (256-bit key) | 
| JWT_EXPIRATION_ACCESS  | `120000`                                    | Access token lifetime (2 min by default)    | 
| JWT_EXPIRATION_REFRESH | `1209600000`                                | Refresh token lifetime (14 days by default) | 
| USE_SWAGGER            | `true`                                      | Use Swagger UI                              | 
| SERVER_PORT            | `8080`                                      | Tomcat server port                          | 
| LOGGING_LEVEL          | `INFO`                                      | Console logging level                       | 
| ALLOWED_ORIGINS        | `http://localhost:3000, http://client:3000` | Allowed clients of api (array)              | 
| RABBITMQ_HOST          | `localhost`                                 | RabbitMQ host url                           | 
| RABBITMQ_PORT          | `5672`                                      | RabbitMQ host port                          | 
| RABBITMQ_USERNAME      | `rabbitmq`                                  | RabbitMQ username                           | 
| RABBITMQ_PASSWORD      | `rabbitmq`                                  | RabbitMQ password                           | 

## Docker Compose

Example of docker-compose task-tracker-api service:

```yaml
version: '3'

services:
  api:
    image: farneser/task-tracker-api:latest
    container_name: api-container
    environment:
      POSTGRES_HOST: localhost
      POSTGRES_PORT: 5432
      POSTGRES_DB: task-tracker
      POSTGRES_USERNAME: postgres
      POSTGRES_PASSWORD: postgres
      JWT_SECRET: F40BB648F9CA2303F6878ACD7CF446A28845426C508BF3CBC06740AD892D7B9B # example 256-bit key
      JWT_EXPIRATION_ACCESS: 120000
      JWT_EXPIRATION_REFRESH: 1209600000
      USE_SWAGGER: true
      SERVER_PORT: 8080
      LOGGING_LEVEL: INFO
      ALLOWED_ORIGINS: http://localhost:3000, http://client:3000
      RABBITMQ_HOST: localhost
      RABBITMQ_PORT: 5672
      RABBITMQ_USERNAME: rabbitmq
      RABBITMQ_PASSWORD: rabbitmq
```