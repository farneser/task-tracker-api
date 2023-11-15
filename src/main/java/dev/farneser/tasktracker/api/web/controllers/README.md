# Queries

The queries will differ from the assignment due to the use of JWT tokens for authentication instead of sessions.
Additionally, the task implies an improvement in skills related to independent project planning and structuring

# Endpoints

## /api/v1/auth

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

## /api/v1/auth/register

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

## /api/v1/auth/refresh

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

## /api/v1/user

`GET`

This endpoint is used to retrieve user information based on a valid access token. Clients can make a GET request to
obtain details about the authenticated user.

response body

```json
{
  "id": "number",
  "email": "string"
}
```
