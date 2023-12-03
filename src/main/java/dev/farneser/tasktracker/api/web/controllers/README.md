# Queries

The queries will differ from the assignment due to the use of JWT tokens for authentication instead of sessions.
Additionally, the task implies an improvement in skills related to independent project planning and structuring

# Endpoints

## Auth

### /api/v1/auth

`POST`

This endpoint is used for user authentication. Clients should provide valid email and password in the request body to
obtain access and refresh tokens.

request body:

```json
{
  "email": "string",
  "password": "string"
}
```

response body:

```json
{
  "access_token": "string",
  "refresh_token": "string"
}
```

### /api/v1/auth/register

`POST`

This endpoint allows users to register by providing a valid email, password, and confirming the password in the request
body. Upon successful registration, the user is issued access and refresh tokens.

request body:

```json
{
  "email": "string",
  "password": "string",
  "confirm_password": "string"
}
```

response body:

```json
{
  "access_token": "string",
  "refresh_token": "string"
}
```

### /api/v1/auth/refresh

`POST`

Clients use this endpoint to refresh their access token by providing a valid access token and refresh token in the
request body. This helps maintain user sessions without requiring them to re-enter credentials.

request body:

```json
{
  "access_token": "string",
  "refresh_token": "string"
}
```

response body:

```json
{
  "access_token": "string",
  "refresh_token": "string"
}
```

## Users

### /api/v1/user

`GET`

This endpoint is used to retrieve user information based on a valid access token. Clients can make a GET request to
obtain details about the authenticated user.

response body

```json
{
  "id": "number",
  "email": "string",
  "registrationDate": "string"
}
```

## Columns

### /api/v1/column

`GET`

This endpoint is used to retrieve columns based on a valid access token. Clients can make a GET request to obtain
details about the columns.

response body

```json
[
  {
    "id": 0,
    "columnName": "string",
    "isCompleted": true,
    "orderNumber": 0,
    "creationDate": "string",
    "editDate": "string",
    "tasks": [
      {
        "id": 0,
        "taskName": "string",
        "description": "string",
        "orderNumber": 0,
        "column": null,
        "creationDate": "string",
        "editDate": "string"
      }
    ]
  }
]
```

`POST`

This endpoint is used to create a new column based on a valid access token. Clients can make a POST request to create a
new column.

request body:

```json
{
  "columnName": "string",
  "isCompleted": true
}
```

response body:

```json
{
  "id": 0,
  "columnName": "string",
  "isCompleted": true,
  "orderNumber": 0,
  "creationDate": "string",
  "editDate": "string",
  "tasks": [
    {
      "id": 0,
      "taskName": "string",
      "description": "string",
      "orderNumber": 0,
      "creationDate": "string",
      "editDate": "string",
      "column": null
    }
  ]
}
```

### /api/v1/column/{id}

`GET`

This endpoint is used to retrieve a column based on a valid access token and column id. Clients can make a GET request
to obtain details about the column.

response body

```json
{
  "id": 0,
  "columnName": "string",
  "isCompleted": true,
  "orderNumber": 0,
  "creationDate": "string",
  "editDate": "string",
  "tasks": [
    {
      "id": 0,
      "taskName": "string",
      "description": "string",
      "orderNumber": 0,
      "column": null,
      "creationDate": "string",
      "editDate": "string"
    }
  ]
}
```

`DELETE`

This endpoint is used to delete a column based on a valid access token and column id. Clients can make a DELETE request
to delete the column.

response body

```json
{
  "message": "string"
}
```

`PATCH`

This endpoint is used to update a column based on a valid access token and column id. Clients can make a PATCH request
to update the column.

request body:

```json
{
  "columnName": "string",
  "isCompleted": true,
  "orderNumber": 0
}
```

response body

```json
{
  "id": 0,
  "columnName": "string",
  "isCompleted": true,
  "orderNumber": 0,
  "creationDate": "string",
  "editDate": "string",
  "tasks": [
    {
      "id": 0,
      "taskName": "string",
      "description": "string",
      "orderNumber": 0,
      "column": null,
      "creationDate": "string",
      "editDate": "string"
    }
  ]
}
```

### /api/v1/column/{id}/tasks

`GET`

This endpoint is used to retrieve tasks based on a valid access token and column id. Clients can make a GET request to
obtain details about the tasks.

response body

```json
[
  {
    "id": 0,
    "taskName": "string",
    "description": "string",
    "orderNumber": 0,
    "creationDate": "string",
    "editDate": "string",
    "column": null
  }
]
```

## Tasks

### /api/v1/task

`GET`

This endpoint is used to retrieve tasks based on a valid access token. Clients can make a GET request to obtain details
about the tasks.

response body

```json
[
  {
    "id": 0,
    "taskName": "string",
    "description": "string",
    "orderNumber": 0,
    "creationDate": "string",
    "editDate": "string",
    "column": {
      "id": 0,
      "columnName": "string",
      "isCompleted": true,
      "orderNumber": 0,
      "creationDate": "string",
      "editDate": "string",
      "tasks": null
    }
  }
]
```

`POST`

This endpoint is used to create a new task based on a valid access token. Clients can make a POST request to create a
new task.

request body:

```json
{
  "columnId": 0,
  "taskName": "string",
  "description": "string"
}
```

response body:

```json
{
  "id": 0,
  "taskName": "string",
  "description": "string",
  "orderNumber": 0,
  "creationDate": "string",
  "editDate": "string",
  "column": {
    "id": 0,
    "columnName": "string",
    "isCompleted": true,
    "orderNumber": 0,
    "tasks": null
  }
}
```

### /api/v1/task/{id}

`GET`

This endpoint is used to retrieve a task based on a valid access token and task id. Clients can make a GET request to
obtain details about the task.

response body

```json
{
  "id": 0,
  "taskName": "string",
  "description": "string",
  "orderNumber": 0,
  "creationDate": "string",
  "editDate": "string",
  "column": {
    "id": 0,
    "columnName": "string",
    "isCompleted": true,
    "orderNumber": 0,
    "creationDate": "string",
    "editDate": "string",
    "tasks": null
  }
}
```

`DELETE`

This endpoint is used to delete a task based on a valid access token and task id. Clients can make a DELETE request to
delete the task.

response body

```json
{
  "message": "string"
}
```

`PATCH`

This endpoint is used to update a task based on a valid access token and task id. Clients can make a PATCH request to
update the task.

request body:

```json
{
  "columnId": 0,
  "taskName": "string",
  "description": "string",
  "orderNumber": 0
}
```

response body

```json
{
  "id": 0,
  "taskName": "string",
  "description": "string",
  "orderNumber": 0,
  "creationDate": "string",
  "editDate": "string",
  "column": {
    "id": 0,
    "columnName": "string",
    "isCompleted": true,
    "orderNumber": 0,
    "creationDate": "string",
    "editDate": "string",
    "tasks": null
  }
}
```

## /api/v1/task/archived

`GET`

This endpoint is used to retrieve archived tasks based on a valid access token. Clients can make a GET request to obtain
details about the archived tasks.

response body

```json
[
  {
    "id": 0,
    "taskName": "string",
    "description": "string",
    "orderNumber": 0,
    "creationDate": "string",
    "editDate": "string",
    "column": {
      "id": 0,
      "columnName": "string",
      "isCompleted": true,
      "orderNumber": 0,
      "creationDate": "string",
      "editDate": "string",
      "tasks": null
    }
  }
]
```