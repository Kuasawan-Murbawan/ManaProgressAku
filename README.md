# ManaProgressAku 🏋️

**ManaProgressAku** is a full-stack fitness progress tracking application designed to help users record, manage, and review their workout sessions in a clean and intuitive way.

The project was built to simulate a real-world production application, covering not only frontend and backend development, but also authentication, deployment, cloud infrastructure, state management, API communication, and documentation.

---

# 🌐 Live Demo

Frontend:

- https://manaprogressaku.com

Backend API:

- https://api.manaprogressaku.com

Swagger API Documentation:

- https://api.manaprogressaku.com/swagger-ui.html

---

# ✨ Features

## Authentication & Security

- User registration
- User login
- JWT-based authentication
- Protected frontend routes
- Secure backend authorization using Spring Security
- Admin-only actions enforced on the backend, not just hidden in the UI

## Workout Tracking

- Create workout sessions and recover an active session after a refresh
- Add exercises to a session
- Record sets, reps, and weights
- Edit logged sets afterward
- Bodyweight exercises fill in your profile weight automatically
- View past workout sessions
- Dynamic workout summaries

## Exercise Library

- Browse exercises by upper and lower body
- Reference image, target and secondary muscles, and step-by-step instructions from ExerciseDB
- Equipment tags
- Admin tools: add, edit and delete exercises, set equipment, flag bodyweight exercises, and link an exercise to its ExerciseDB entry

## Profile & Account

- Optional profile: weight, height, date of birth, and gender
- Delete account (asks for your password and removes all of your workout data)

## User Experience

- Responsive modern UI with a bottom navigation bar on mobile and a top bar on desktop
- Loading states
- Persistent authentication state
- Clean session history interface
- Real-time frontend state management

## Cloud Deployment

- Frontend hosted on AWS S3 + CloudFront
- Backend hosted on AWS Elastic Beanstalk
- MySQL database hosted on AWS RDS

---

# 🧠 Why This Project Exists

ManaProgressAku was created as a portfolio-grade application to explore how modern full-stack systems work together in production.

Instead of focusing only on CRUD operations, the project also emphasizes:

- authentication architecture
- API design
- cloud deployment
- frontend/backend communication
- state management
- infrastructure troubleshooting
- maintainable project structure

---

# 🛠️ Tech Stack

## Frontend

- React
- Vite
- Chakra UI
- Zustand
- Axios
- React Router

## Backend

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- JPA / Hibernate
- Swagger / OpenAPI

## Database

- MySQL
- Amazon RDS

## External Services

- ExerciseDB v2 by AscendAPI (through RapidAPI) for exercise images, muscles, and instructions

## Testing

- Postman collection run with Newman

## Cloud & Infrastructure

- AWS S3
- AWS CloudFront
- AWS Elastic Beanstalk
- AWS RDS

---

# 🚀 How The Application Works

## 1. Authentication Flow

1. User logs in using email and password
2. Backend validates credentials
3. JWT token is generated
4. Frontend stores token using Zustand persistence
5. Axios interceptor automatically attaches JWT to protected requests
6. Backend validates token before allowing access

---

## 2. Workout Session Flow

1. User starts a new workout session
2. User selects exercises
3. User records weight and reps for each set
4. Activities are stored in the backend
5. User can review workout history from the Past Sessions page

---

## 3. Exercise Information Flow

1. An admin links a library exercise to its ExerciseDB entry (search, pick, save). Only the ExerciseDB ID is stored
2. When a user opens a linked exercise, the backend returns the image, muscles, and instructions, served from a 55-minute in-memory cache when possible
3. If the exercise is not linked, or ExerciseDB is unavailable or over its quota, the app shows its normal placeholder instead of an error

Exercise images, muscle data, and instructions are provided by ExerciseDB (AscendAPI). They are fetched on demand and never stored.

---

# 📚 Documentation

This repository also contains additional technical documentation:

| Document                                                                                                                | Purpose                                                                           |
| ----------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
| [docs/ARCHITECTURE.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/ARCHITECTURE.md)           | System architecture, data model, the ExerciseDB integration, and deployment notes |
| [docs/CHANGELOG.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/CHANGELOG.md)                 | Project release history and upgrade notes                                         |
| [docs/DESIGN_PHILOSOPHY.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/DESIGN_PHILOSOPHY.md) | Visual design system and UI conventions                                           |
| Swagger UI                                                                                                              | Detailed API endpoint documentation                                               |

---

# 🧪 Future Improvements

Planned features for future releases include:

- Google sign-in
- Workout analytics and insights, including profile data such as age and weight
- Filtering the exercise library by equipment and muscle group
- Wider exercise coverage beyond the ExerciseDB free plan
- CI/CD that runs the API test suite on every push

---

# 🏗️ Local Development Setup

## Frontend

```bash
cd Frontend
npm install
npm run dev
```

## Backend

`application.properties` is gitignored, so create it under `src/main/resources` and supply the environment variables below.

| Variable                                                  | Purpose                                                                                                         |
| --------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| `JWT_SECRET_KEY`                                          | Secret used to sign JWTs                                                                                        |
| `RAPIDAPI_KEY`                                            | RapidAPI key for the ExerciseDB v2 subscription                                                                 |
| `RDS_HOSTNAME`, `RDS_PORT`, `RDS_DB_NAME`, `RDS_USERNAME` | Database connection. They default to a local MySQL (`localhost:3306`, database `progress_tracker`, user `root`) |
| `RDS_PASSWORD`                                            | Database password (no default)                                                                                  |

```properties
spring.application.name=ManaProgressAku
server.port=${PORT:8080}

spring.datasource.url=jdbc:mysql://${RDS_HOSTNAME:localhost}:${RDS_PORT:3306}/${RDS_DB_NAME:progress_tracker}
spring.datasource.username=${RDS_USERNAME:root}
spring.datasource.password=${RDS_PASSWORD}
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
security.jwt.secret-key=${JWT_SECRET_KEY}
security.jwt.expiration-time=3600000
rapidapi.exercisedb.key=${RAPIDAPI_KEY}
rapidapi.exercisedb.host=edb-with-videos-and-images-by-ascendapi.p.rapidapi.com
rapidapi.exercisedb.base-url=https://edb-with-videos-and-images-by-ascendapi.p.rapidapi.com/api/v1
```

```bash
cd Backend
mvn spring-boot:run
```

Hibernate creates missing tables and columns on startup, but foreign key delete rules and the single seed row in `ascend_api_usage` must be applied by hand. See [docs/ARCHITECTURE.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/ARCHITECTURE.md).

## API Tests

From the repository root:

```bash
npm install
npm run test:api:local
```

Put `ADMIN_EMAIL` and `ADMIN_PW` in a gitignored `.env.local` file at the repository root. `npm run test:api:prod` runs the same collection against production and creates and deletes real rows, so run it deliberately.
