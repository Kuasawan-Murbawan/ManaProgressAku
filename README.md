# ManaProgressAku 🏋️

![CI](https://github.com/Kuasawan-Murbawan/ManaProgressAku/actions/workflows/ci.yml/badge.svg)

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

- JUnit 5
- Mockito
- Testcontainers (MySQL 8)
- JaCoCo
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
- Automated deploy from GitHub Actions (the checks already run on every pull request)

---

# 🏗️ Local Development Setup

## Frontend

```bash
cd Frontend
npm install
npm run dev
```

## Backend

`application.properties` is committed in `src/main/resources`, so there is nothing to create. It holds no secrets: they come from the environment variables below. `JWT_SECRET_KEY`, `RAPIDAPI_KEY` and `RDS_PASSWORD` have no default, so they must be set.

For local runs they can go in `Backend/config/application.properties`, which is gitignored. Spring reads that file when the app starts from the `Backend` folder.

| Variable                                                  | Purpose                                                                                                         |
| --------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| `JWT_SECRET_KEY`                                          | Secret used to sign JWTs (no default)                                                                           |
| `RAPIDAPI_KEY`                                            | RapidAPI key for the ExerciseDB v2 subscription (no default)                                                    |
| `RDS_PASSWORD`                                            | Database password (no default)                                                                                  |
| `RDS_HOSTNAME`, `RDS_PORT`, `RDS_DB_NAME`, `RDS_USERNAME` | Database connection. They default to a local MySQL (`localhost:3306`, database `progress_tracker`, user `root`) |
| `PORT`                                                    | HTTP port. It defaults to `8080`                                                                                |

The committed file:

```properties
spring.application.name=ManaProgressAku
server.port=${PORT:8080}

spring.datasource.url=jdbc:mysql://${RDS_HOSTNAME:localhost}:${RDS_PORT:3306}/${RDS_DB_NAME:progress_tracker}
spring.datasource.username=${RDS_USERNAME:root}
spring.datasource.password=${RDS_PASSWORD}
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
#spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=validate
security.jwt.secret-key=${JWT_SECRET_KEY}
security.jwt.expiration-time=3600000
rapidapi.exercisedb.key=${RAPIDAPI_KEY}
rapidapi.exercisedb.host=edb-with-videos-and-images-by-ascendapi.p.rapidapi.com
rapidapi.exercisedb.base-url=https://edb-with-videos-and-images-by-ascendapi.p.rapidapi.com/api/v1

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=1
```

```bash
cd Backend
mvn spring-boot:run
```

Flyway applies the schema at start-up from `Backend/src/main/resources/db/migration`, so there is no SQL to run by hand. Hibernate only validates the schema. See [docs/ARCHITECTURE.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/ARCHITECTURE.md).

## Testing

Run these in the `Backend` folder.

- `mvn test` runs the unit and web tests (classes ending in `Test`). No Docker needed, about 10 seconds.
- `mvn clean verify` runs everything: those tests, then the database tests (classes ending in `IT`) against a real MySQL 8 container that Testcontainers starts, then it writes the coverage report. Docker must be running. No environment variables are needed, because the test profile has fake values. The release uses this command.
- `mvn -Dtest=JwtAuthenticationFilterTest test` runs one unit test class.
- `mvn -Dtest=NoSuchTest -Dsurefire.failIfNoSpecifiedTests=false -Dit.test=CascadeDeleteIT verify` runs one database test class.

Reports: `target/surefire-reports` (unit and web), `target/failsafe-reports` (database), `target/site/jacoco/index.html` (coverage). The test schema is in `src/test/resources/db/migration`.

What is covered: the authentication, profile and exercise services, JWT handling, the exercise cache and the API rate limiter; the status code of every protected endpoint with no token (401) and of every admin endpoint for a normal user (403); the security filter; and a real database for the schema, the delete cascades and the quota row lock. ExerciseDB is always faked, so no test spends API quota. Not covered yet: the session, activity and set services and controllers (the workout code), which are the next area to test, and the handler bodies in `ProfileController` and `ExerciseDetailsController`, which only have access checks. The Newman collection in `Tests/API` stays as the black-box check of the running app.

Rules: every bug fix comes with a regression test that fails without the fix. A new endpoint outside `/auth` needs a row in `EndpointSecurityTest`, which fails until you add it. Never put a real secret in a test or in `application-test.properties`.

## API Tests

From the repository root:

```bash
npm install
npm run test:api:local
```

Put `ADMIN_EMAIL`, `ADMIN_PW`, `TEST_USER_EMAIL` and `TEST_USER_PW` in a gitignored `.env.local` file at the repository root. `npm run test:api:prod` reads the same variables from `.env.prod` and runs the same collection against production. It creates and deletes real rows, so run it deliberately.

---

# ✅ Continuous Integration

Every pull request to `master` runs three checks in GitHub Actions (`.github/workflows/ci.yml`):

| Check                   | What it does                                                                                                   |
| ----------------------- | -------------------------------------------------------------------------------------------------------------- |
| Backend tests           | `./mvnw clean verify` in `Backend`: unit, web and database tests. The jar that passes is kept for the next job |
| Frontend lint and build | `npm ci`, `npm run lint` and `npm run build` in `Frontend`                                                     |
| API smoke (Newman)      | Starts that jar with a throwaway MySQL and a fake ExerciseDB, then runs the Postman collection                 |

`master` is protected. Changes arrive only through a pull request, and all three checks must be green on a branch that is up to date with `master`. The rule has no bypass list.

Dependabot opens a pull request each week when a GitHub Action used by the workflow has a new version.

## Run the checks locally

Newman, from the repository root, with the backend running on `localhost:8080`:

```bash
npm install
npm run test:api:local
```

`.env.local` must hold `ADMIN_EMAIL` and `ADMIN_PW` for an admin account, and `TEST_USER_EMAIL` and `TEST_USER_PW` for a normal user that already exists in that database.

Fake ExerciseDB (WireMock with the stubs in `Tests/wiremock`), from the repository root:

```bash
docker run -p 8089:8080 -v "$PWD/Tests/wiremock:/home/wiremock" wiremock/wiremock:3.9.1
```

Then start the backend with `RAPIDAPI_EXERCISEDB_BASE_URL=http://localhost:8089/api/v1` so it calls the stubs instead of RapidAPI.

Fresh local database with the CI data, from the `Backend` folder against an empty database:

```bash
cd Backend
export SPRING_FLYWAY_LOCATIONS=classpath:db/migration,filesystem:ci-seed
export ADMIN_EMAIL=admin@example.test
export ADMIN_PW=choose-a-password
mvn spring-boot:run
```

When you stop the app, clear them with `unset SPRING_FLYWAY_LOCATIONS ADMIN_EMAIL ADMIN_PW`, so the next run in that terminal does not pick them up.

Flyway adds the 15 default exercises from `Backend/ci-seed`, and `AdminBootstrap` creates the admin from `ADMIN_EMAIL` and `ADMIN_PW`. Use this only on a throwaway database. Flyway records the seed as version 1000, so that database fails Flyway validation if it later starts without `ci-seed`, and it refuses any later migration numbered below 1000 (V3, V4 and so on). Drop and recreate such a database when you are done. Never keep it.

---

# 📦 Deploy

The scripts read their settings from `scripts/config.sh`. That file is private and ignored by Git, so it is never committed. Create it once by copying `scripts/config.example.sh` and filling in the AWS region, the Elastic Beanstalk application, environment and bucket, the frontend bucket, the CloudFront distribution and the API URL. The AWS CLI must be installed and logged in.

## 1. Build the jar from a clean clone

Build from a fresh clone, so the jar contains only committed code. A tag is the normal choice for a release, so the build always matches what was released.

```bash
git clone --branch <tag or branch> https://github.com/Kuasawan-Murbawan/ManaProgressAku.git ManaProgressAku-release
cd ManaProgressAku-release
cp <your working copy>/scripts/config.sh scripts/config.sh
cd Backend
mvn clean verify
cd ..
```

`mvn clean verify` runs every test (Docker must be running) and leaves the jar in `Backend/target`.

## 2. Deploy the backend

```bash
scripts/deploy-backend.sh v1.8.0
```

It uploads the jar, creates a new Elastic Beanstalk version labelled `<version>-<date>-<time>`, and waits until `/actuator/health` answers. Before it starts, it prints the label that is currently running. Keep it: that is your rollback target.

## 3. Deploy the frontend

```bash
scripts/deploy-frontend.sh
```

It runs `npm ci`, `npm run lint` and `npm run build` in `Frontend` (Node 22), uploads the build to S3 and invalidates CloudFront.

## Roll back the backend

```bash
scripts/rollback-backend.sh <version label>
```

Use the label printed by `deploy-backend.sh`. The comment at the top of `scripts/rollback-backend.sh` shows how to list every label. A rollback changes only the code. Flyway migrations that already ran stay in the database.
