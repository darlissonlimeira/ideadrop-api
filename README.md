## Usage

## Prerequisites

- Java 21
- Docker

## Environment Variables

All variables have default values. To change any default value, update the ```docker-compose.yml``` file.

Optional: rename example.env to .env and add the following:

```
PORT=
DATABASE_URL=
DATABASE_USER=
DATABASE_PASSWORD=
ALLOWED_ORIGINS=
JWT_SECRET=
REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
```

## Run Server

To use default values, environment variables from host. If you want to use a ``.env`` file, rename the ``.env.example``
to ``.env`` and fill the variables:

```bash
  docker compose up -d
```

## How Authentication Works

The user hits the `/login` or `/register` endpoint and gets back the user and a short-lived access token that is valid
for 1 minute (you can change if you want) in the response. It also stores a long-lived (30 days) refresh token in an
HTTP-only cookie, which can be used to generate access tokens.

The access token can be sent in the header authorization as a bearer token to access protected routes such as
`POST /api/ideas`. It expires after a short time and then you can hit the `/api/auth/refresh` endpoint and it will
generate a new access token using the refresh token in the HTTP-only cookie.

## Routes

### 🔐 Auth Routes (`/api/auth`)

| Method | Endpoint    | Description                              | Auth Required  |
|--------|-------------|------------------------------------------|----------------|
| POST   | `/register` | Register a new user                      | ❌ No           |
| POST   | `/login`    | Log in an existing user                  | ❌ No           |
| POST   | `/logout`   | Log out and clear refresh token          | ✅ Yes (cookie) |
| POST   | `/refresh`  | Get new access token using refresh token | ✅ Yes (cookie) |

> 📝 The refresh token is stored in an **HTTP-only cookie**.  
> 🔐 Access tokens must be sent as a **Bearer token** in the `Authorization` header for protected routes.

---

### 💡 Idea Routes (`/api/ideas`)

| Method | Endpoint | Description                    | Auth Required        |
|--------|----------|--------------------------------|----------------------|
| GET    | `/`      | Get all public ideas           | ❌ No                 |
| GET    | `/:id`   | Get a single idea by ID        | ❌ No                 |
| POST   | `/`      | Create a new idea              | ✅ Yes (access token) |
| PUT    | `/:id`   | Update an idea (only if owner) | ✅ Yes (access token) |
| DELETE | `/:id`   | Delete an idea (only if owner) | ✅ Yes (access token) |