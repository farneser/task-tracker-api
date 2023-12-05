## Statuses

These tags categorize error response statuses providing detailed insights into client-side issues rather than
traditional HTTP statuses.

### 200 SUCCESS

Indicates a successful request where the server responds with a JSON-formatted body. Everything is functioning as
expected.

### 401 UNAUTHORIZED

The request lacks valid authentication credentials, preventing access to the requested resource.

### 4011 ACCESS_TOKEN_EXPIRED

Specifically denotes an unauthorized request due to an expired access token. Users need to re-authenticate for continued
access.

### 4012 REFRESH_TOKEN_EXPIRED

Highlights an unauthorized request resulting from an expired refresh token. Users should refresh their authentication
tokens to regain access.

### 403 FORBIDDEN

The server understood the request but refuses to authorize it. This is often due to the user not having the necessary.
For example user is not enabled.

### 404 NOT_FOUND

The requested resource does not exist. The server could not find the resource matching the provided path.

### 500 SERVER_ERROR

Signals an internal server error. Something went wrong on the server side, and the request could not be completed.
Further investigation is needed to identify and resolve the issue.