# K-Help — Korea Life Platform for Foreign Residents

Team **NP Coders** · Stack: Next.js + Spring Boot + PostgreSQL + Redis

Read `AGENT.md` before writing code. It is the source of truth for layout, APIs, and milestones.

## Quick start

### 1. Start databases
```powershell
cd D:\NP-CODERS\K-Help
docker compose up -d
```

### 2. Start backend (port 8080)
```powershell
cd D:\NP-CODERS\K-Help\backend
.\mvnw.cmd spring-boot:run
```

### 3. Start frontend (port 3000)
```powershell
cd D:\NP-CODERS\K-Help\frontend
npm install
npm run dev
```

Open http://localhost:3000 — the home page shows a live backend health check.

## Working APIs (V1/V2 baseline)

| Method | Path | Auth |
|--------|------|------|
| GET | `/api/hello` | Public |
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/users/me` | Bearer JWT |

Example register body:
```json
{
  "email": "bibek@example.com",
  "password": "password123",
  "nickname": "bibek",
  "visaType": "D-2",
  "nationality": "Nepal"
}
```

## Project layout
- `frontend/src/` — Next.js App Router UI
- `backend/src/main/java/com/example/demo/` — Spring Boot API
- `backend/src/main/resources/db/migration/` — Flyway SQL
- `docker-compose.yml` — PostgreSQL 15 + Redis 7

## If Flyway fails after schema changes
Reset the local DB volume and restart:
```powershell
docker compose down -v
docker compose up -d
```
Then run the backend again so Flyway recreates tables.
