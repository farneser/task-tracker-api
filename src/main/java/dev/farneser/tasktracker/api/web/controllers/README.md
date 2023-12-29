# Web Api description

The queries will differ from the assignment due to the use of JWT tokens for authentication instead of sessions.
Additionally, the task implies an improvement in skills related to independent project planning and structuring

# Endpoints

## Auth

### /api/v1/auth

#### POST

- **Tags:** auth-controller
- **Summary:** Authenticate user
- **Description:** Authenticate user and return JWT token
- **Operation ID:** authenticate
- **Request Body:**
    - Content:
        - `application/json`:
            - Schema: [LoginRequest](#LoginRequest)
    - Required: true
- **Responses:**
    - 404:
        - Description: User not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: Bad credentials
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Invalid credentials
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully authenticated user
        - Content:
            - `application/json`:
                - Schema: [JwtDto](#JwtDto)

### /api/v1/auth/register

#### POST

- **Tags:** auth-controller
- **Summary:** Register user
- **Description:** Register user and return JWT token
- **Operation ID:** register
- **Request Body:**
    - Content:
        - `application/json`:
            - Schema: [RegisterDto](#RegisterDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: Not Found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: Unauthorized
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: User already exists
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Invalid credentials
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 201:
        - Description: Successfully registered user
        - Content:
            - `application/json`:
                - Schema: [JwtDto](#JwtDto)

### /api/v1/auth/refresh

#### POST

- **Tags:** auth-controller
- **Summary:** Refresh JWT token
- **Description:** Refresh JWT token and return new JWT token
- **Operation ID:** refresh
- **Request Body:**
    - Content:
        - `application/json`:
            - Schema: [JwtDto](#JwtDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: User not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Invalid JWT token
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully refreshed JWT token
        - Content:
            - `application/json`:
                - Schema: [JwtDto](#JwtDto)

### /api/v1/auth/confirm

#### POST

- **Tags:** auth-controller
- **Summary:** Confirm email
- **Description:** Confirm email and activate account
- **Operation ID:** confirm
- **Parameters:**
    - Name: token
        - In: query
        - Description: Confirm email token (uuid)
        - Required: true
        - Schema:
            - Type: string
            - Format: uuid
- **Responses:**
    - 404:
        - Description: User not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: Unauthorized
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully confirmed email
        - Content:
            - `application/json`:
                - Schema: [Message](#Message)

## Users

### /api/v1/user

#### GET

- **Tags:** user-controller
- **Summary:** Get user
- **Description:** Get user by JWT token
- **Operation ID:** get
- **Responses:**
    - 404:
        - Description: User not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got user
        - Content:
            - `application/json`:
                - Schema: [UserView](#UserView)

#### PATCH

- **Tags:** user-controller
- **Summary:** Patch user
- **Description:** Patch user data
- **Operation ID:** patchBy
- **Request Body:**
    - Content:
        - application/json:
            - Schema: [PatchUserDto](#PatchUserDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: User not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully patched user
        - Content:
            - `application/json`:
                - Schema: [UserView](#UserView)

## Columns

### /api/v1/column

#### GET

- **Tags:** column-controller
- **Summary:** Get columns
- **Description:** Get columns by JWT token
- **Operation ID:** get_2
- **Parameters:**
    - Name: retrieveTasks
        - In: query
        - Description: Toggles inclusion of current task details in the response
        - Required: false
        - Schema:
            - Type: boolean
            - Default: true
- **Responses:**
    - 404:
        - Description: Columns not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got columns
        - Content:
            - `application/json`:
                - Schema:
                    - Type: array
                    - Items: [ColumnView](#ColumnView)

#### POST

- **Tags:** column-controller
- **Summary:** Create column
- **Description:** Create column
- **Operation ID:** create_1
- **Request Body:**
    - Content:
        - `application/json`:
            - Schema: [CreateColumnDto](#CreateColumnDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: Not Found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 201:
        - Description: Successfully created column
        - Content:
            - `application/json`:
                - Schema: [ColumnView](#ColumnView)

### /api/v1/column/{id}

#### GET

- **Tags:** column-controller
- **Summary:** Get column
- **Description:** Get column by id
- **Operation ID:** getById_1
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Responses:**
    - 404:
        - Description: Column not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got column
        - Content:
            - `application/json`:
                - Schema: [ColumnView](#ColumnView)

#### DELETE

- **Tags:** column-controller
- **Summary:** Delete column
- **Description:** Delete column by id
- **Operation ID:** deleteById_1
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Responses:**
    - 404:
        - Description: Column not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully deleted column
        - Content:
            - `application/json`:
                - Schema: [Message](#Message)

#### PATCH

- **Tags:** column-controller
- **Summary:** Patch column
- **Description:** Patch column data
- **Operation ID:** patchById_1
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Request Body:**
    - Content:
        - application/json:
            - Schema: [PatchColumnDto](#PatchColumnDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: Column not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully patched column
        - Content:
            - `application/json`:
                - Schema: [ColumnView](#ColumnView)

### /api/v1/column/{id}/tasks

#### GET

- **Tags:** column-controller
- **Summary:** Get tasks
- **Description:** Get tasks by column id
- **Operation ID:** getTasksById
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Responses:**
    - 404:
        - Description: Tasks not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got tasks
        - Content:
            - `application/json`:
                - Schema:
                    - Type: array
                    - Items: [TaskView](#TaskView)

## Tasks

### /api/v1/task

#### GET

- **Tags:** task-controller
- **Summary:** Get tasks
- **Description:** Get tasks by JWT token
- **Operation ID:** get_1
- **Responses:**
    - 404:
        - Description: Not Found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got tasks
        - Content:
            - `application/json`:
                - Schema:
                    - Type: array
                    - Items: [TaskLookupView](#TaskLookupView)

#### POST

- **Tags:** task-controller
- **Summary:** Create task
- **Description:** Create task
- **Operation ID:** create
- **Request Body:**
    - Content:
        - `application/json`:
            - Schema: [CreateTaskDto](#CreateTaskDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: Column not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 201:
        - Description: Successfully created task
        - Content:
            - `application/json`:
                - Schema: [TaskView](#TaskView)

### /api/v1/task/{id}

#### GET

- **Tags:** task-controller
- **Summary:** Get task
- **Description:** Get task by id
- **Operation ID:** getById
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Responses:**
    - 404:
        - Description: Task not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got task
        - Content:
            - `application/json`:
                - Schema: [TaskView](#TaskView)

#### DELETE

- **Tags:** task-controller
- **Summary:** Delete task
- **Description:** Delete task by id
- **Operation ID:** deleteById
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Responses:**
    - 404:
        - Description: Task not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully deleted task
        - Content:
            - `application/json`:
                - Schema: [Message](#Message)

#### PATCH

- **Tags:** task-controller
- **Summary:** Patch task
- **Description:** Patch task by id
- **Operation ID:** patchById
- **Parameters:**
    - name: id
        - in: path
        - required: true
        - schema:
            - type: integer
            - format: int64
- **Request Body:**
    - Content:
        - application/json:
            - Schema: [PatchTaskDto](#PatchTaskDto)
    - Required: true
- **Responses:**
    - 404:
        - Description: Task not found or Column not found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully patched task
        - Content:
            - `application/json`:
                - Schema: [TaskView](#TaskView)

### /api/v1/task/archive

#### GET

- **Tags:** task-controller
- **Summary:** Get archived tasks
- **Description:** Get archived tasks by JWT token
- **Operation ID:** getArchived
- **Responses:**
    - 404:
        - Description: Not Found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully got archived tasks
        - Content:
            - `application/json`:
                - Schema:
                    - Type: array
                    - Items: [TaskLookupView](#TaskLookupView)

#### PUT

- **Tags:** task-controller
- **Summary:** Archive tasks
- **Description:** Archive tasks
- **Operation ID:** archieTasks
- **Responses:**
    - 404:
        - Description: Not Found
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 403:
        - Description: Forbidden
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 500:
        - Description: Internal Server Error
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 401:
        - Description: JWT token expired or invalid
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 409:
        - Description: Conflict
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 415:
        - Description: Unsupported Media Type
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 400:
        - Description: Bad Request
        - Content:
            - `application/json`:
                - Schema: [ErrorResponse](#ErrorResponse)
    - 200:
        - Description: Successfully archived tasks
        - Content:
            - `application/json`:
                - Schema: [Message](#Message)

# Schemas

## ErrorResponse

- **Type:** object
- **Properties:**
    - `status`
        - Type: integer
        - Description: Error status
        - Format: int32
        - Example: 400
    - `message`
        - Type: string
        - Description: Error message
        - Example: "Bad request"
- **Description:** Error response DTO

## Message

- **Type:** object
- **Properties:**
    - `message`
        - Type: string
        - Description: Message
        - Example: "Hello world"
- **Description:** Message DTO

## CreateTaskDto

- **Type:** object
- **Properties:**
    - `columnId`
        - Type: integer
        - Description: Column id
        - Format: int64
        - Example: 1
    - `taskName`
        - Type: string
        - Description: Task name
        - Example: "Do something"
    - `description`
        - Type: string
        - Description: Task description
        - Example: "Do something with something"
- **Description:** Create task DTO

## ColumnView

- **Type:** object
- **Properties:**
    - `id`
        - Type: integer
        - Description: Column id
        - Format: int64
        - Example: 1
    - `columnName`
        - Type: string
        - Description: Column name
        - Example: "To do"
    - `isCompleted`
        - Type: boolean
        - Description: Is column completed
        - Example: false
    - `orderNumber`
        - Type: integer
        - Description: Column order number
        - Format: int64
        - Example: 1
    - `tasks`
        - Type: array
        - Description: Column tasks
        - Items: [TaskLookupView](#TaskLookupView)
    - `creationDate`
        - Type: string
        - Description: Column creation date
        - Format: date-time
        - Example: "2021-01-01T00:00:00Z"
    - `editDate`
        - Type: string
        - Description: Column edit date
        - Format: date-time
        - Example: "2021-01-01T00:00:00Z"
- **Description:** Column view

## TaskLookupView

- **Type:** object
- **Properties:**
    - `id`
        - Type: Long
        - Description: Task id
        - Example: 1
    - `taskName`
        - Type: string
        - Description: Task name
        - Example: "Do something"
    - `description`
        - Type: string
        - Description: Task description
        - Example: "Do something with something"
    - `orderNumber`
        - Type: Long
        - Description: Task order number
        - Example: 1
    - `columnId`
        - Type: Long
        - Description: Task column id
        - Example: 1
    - `creationDate`
        - Type: Date
        - Description: Task creation date
        - Example: "2021-01-01T00:00:00.000Z"
    - `editDate`
        - Type: Date
        - Description: Task edit date
        - Example: "2021-01-01T00:00:00.000Z"
    - **Description:** Task lookup view

## TaskView

- **Type:** object
- **Properties:**
    - `id`
        - Type: integer
        - Description: Task id
        - Format: int64
        - Example: 1
    - `taskName`
        - Type: string
        - Description: Task name
        - Example: "Do something"
    - `description`
        - Type: string
        - Description: Task description
        - Example: "Do something with something"
    - `orderNumber`
        - Type: integer
        - Description: Task order number
        - Format: int64
        - Example: 1
    - `column`
        - Type: [ColumnView](#ColumnView)
    - `creationDate`
        - Type: string
        - Description: Task creation date
        - Format: date-time
        - Example: "2021-01-01T00:00:00Z"
    - `editDate`
        - Type: string
        - Description: Task edit date
        - Format: date-time
        - Example: "2021-01-01T00:00:00Z"
- **Description:** Task view

## CreateColumnDto

- **Type:** object
- **Properties:**
    - `columnName`
        - Type: string
        - Description: Column name
        - Example: "To do"
    - `isCompleted`
        - Type: boolean
        - Description: Is column completed
        - Example: false
- **Description:** Create column DTO

## LoginRequest

- **Type:** object
- **Properties:**
    - `email`
        - Type: string
        - Description: User email
        - Example: "example@email.com"
    - `password`
        - maxLength: 64
        - minLength: 8
        - Type: string
        - Description: User password
        - Example: "password"
- **Description:** Login request DTO

## JwtDto

- **Type:** object
- **Properties:**
    - `accessToken`
        - Type: string
        - Description: JWT access token
        - Example: "your_access_token"
    - `refreshToken`
        - Type: string
        - Description: JWT refresh token
        - Example: "your_refresh_token"
- **Description:** JWT token DTO

## RegisterDto

- **Type:** object
- **Properties:**
    - `email`
        - Type: string
        - Description: User email
        - Example: "example@email.com"
    - `password`
        - maxLength: 64
        - minLength: 8
        - Type: string
        - Description: User password
        - Example: "password"
    - `confirmPassword`
        - maxLength: 64
        - minLength: 8
        - Type: string
        - Description: User password confirmation
        - Example: "password"
- **Description:** Register DTO

## PatchUserDto

- **Type:** object
- **Properties:**
    - `isSubscribed`
        - Type: boolean
        - Description: Is user subscribed for the email notification
        - Example: false
- **Description:** Patch user DTO

## UserView

- **Type:** object
- **Properties:**
    - `id`
        - Type: integer
        - Description: User id
        - Format: int64
        - Example: 1
    - `email`
        - Type: string
        - Description: User email
        - Example: "example@email.com"
    - `isSubscribed`
        - Type: boolean
        - Description: Is user subscribed for the email notification
        - Example: false
    - `registrationDate`
        - Type: string
        - Description: User registration date
        - Format: date-time
        - Example: "2021-01-01T00:00:00Z"
    - `enabled`
        - Type: boolean
- **Description:** User view

## PatchTaskDto

- **Type:** object
- **Properties:**
    - `taskName`
        - Type: string
        - Description: Task name
        - Example: "Do something"
    - `description`
        - Type: string
        - Description: Task description
        - Example: "Do something with something"
    - `columnId`
        - Type: integer
        - Description: Column id
        - Format: int64
        - Example: 1
    - `orderNumber`
        - Type: integer
        - Description: Task order number
        - Format: int64
        - Example: 1
- **Description:** Patch task DTO

## PatchColumnDto

- **Type:** object
- **Properties:**
    - `columnName`
        - Type: string
        - Description: Column name
        - Example: "To do"
    - `isCompleted`
        - Type: boolean
        - Description: Is column completed
        - Example: false
    - `orderNumber`
        - Type: integer
        - Description: Column order number
        - Format: int64
        - Example: 1
- **Description:** Patch column DTO
