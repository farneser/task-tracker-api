# Task Tracker API Module: Simple Kanban for Individual Productivity

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

| Parameter         | Default value                               | Description                          |
|-------------------|---------------------------------------------|--------------------------------------|
| POSTGRES_HOST     | `localhost`                                 | IP address of the Postgres DB server |
| POSTGRES_PORT     | `5432`                                      | Port of the Postgres DB server       |
| POSTGRES_DB       | `task-tracker`                              | Postgres database name               |
| POSTGRES_USERNAME | `postgres`                                  | Postgres username                    |
| POSTGRES_PASSWORD | `postgres`                                  | Postgres password                    |
| JWT_SECRET        |                                             | JSON Web Token secret encoder        | 
| USE_SWAGGER       | `true`                                      | Use Swagger UI                       | 
| SERVER_PORT       | `8080`                                      | Tomcat server port                   | 
| LOGGING_LEVEL     | `INFO`                                      | Console logging level                | 
| ALLOWED_ORIGINS   | `http://localhost:3000, http://client:3000` | Allowed clients of api (array)       | 
