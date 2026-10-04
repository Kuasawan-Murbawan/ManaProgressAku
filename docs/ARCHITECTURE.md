# ManaProgressAku Architecture

## Overview

ManaProgressAku is a full-stack fitness tracking application that allows users to:

- register/login
- keep an optional profile (weight, height, date of birth, gender) and delete their account
- create workout sessions
- add exercises and track sets, reps and weight
- edit logged sets afterward
- review workout history
- browse an exercise library with reference images, target muscles and instructions

Admins can also manage the exercise library: create, edit and delete exercises, set equipment, flag bodyweight exercises, and link an exercise to its ExerciseDB entry.

The application follows a client-server architecture and has one external dependency, the ExerciseDB v1 API (built by AscendAPI, sold through RapidAPI).

---

## System Overview

```text
Browser -> CloudFront -> S3                         React single page app
Browser -> Elastic Beanstalk (Spring Boot API) -> RDS MySQL
                                               -> ExerciseDB v2 via RapidAPI (HTTPS)
Browser -> cdn.exercisedb.dev                       exercise images, loaded directly
```

Exercise images are loaded by the browser straight from the ExerciseDB CDN. They never pass through the backend, and the provider does not count them against the API quota.

---

## Frontend

Technology:

- React
- Vite
- Chakra UI
- Zustand
- Axios
- React Router

Responsibilities:

- Render UI
- Handle authentication state
- Manage routing
- Communicate with backend APIs

Frontend deployment:

- AWS S3
- CloudFront CDN

### App shell and routing

Routes are defined in `main.jsx` with `createBrowserRouter`.

- `/login` and `/register` are public and sit outside the app shell, so they never show the navigation bar.
- Every other route is a child of `App`, wrapped in `ProtectedRoute`.
- `App.jsx` renders the `NavBar` (bottom tab bar on mobile, top bar on desktop) and the page outlet. It also checks for an active session on every route change and opens `ActiveSessionDialog` when one exists. The check is skipped on `/createSession`, `/currentActivity` and `/newExercise`, because being on those pages already means the user is working in that session.

| Route                 | Page                | Data loaded on mount                                                           |
| --------------------- | ------------------- | ------------------------------------------------------------------------------ |
| `/`                   | Home                | exercise catalogue (if empty)                                                  |
| `/exerciseList`       | Exercise Library    | exercise catalogue; ExerciseDB details of the opened exercise (only if linked) |
| `/createSession`      | Current session     | session details (session and its activities), user display name                |
| `/newExercise`        | Exercise picker     | exercise catalogue (if not loaded)                                             |
| `/currentActivity`    | Logging sets        | user profile; ExerciseDB details (only if linked)                              |
| `/pastSessions`       | Past sessions       | session list, exercise catalogue                                               |
| `/session/:sessionID` | Past session detail | session details, exercise catalogue                                            |
| `/profile`            | Profile             | user profile                                                                   |

### State management

Each Zustand store owns one concern. Pages read data from the store that owns it and do not keep their own copies.

| Store           | Owns                                                      | Persisted              |
| --------------- | --------------------------------------------------------- | ---------------------- |
| auth            | JWT and the user's role                                   | yes                    |
| user            | display name of the signed-in user                        | no                     |
| session         | active session ID, session list, current session metadata | active session ID only |
| activity        | activities (with their sets) of the session being viewed  | no                     |
| set             | the insert-set API call                                   | no                     |
| exercise        | exercise catalogue; create, edit, delete and link actions | catalogue only         |
| exerciseDetails | ExerciseDB enrichment per exercise, admin search results  | no                     |
| profile         | the user's profile and account deletion                   | no                     |

Rules the stores follow:

- Persisted state is kept small on purpose: the JWT, the active session ID and the exercise catalogue. Everything else is refetched on mount so a refresh cannot show stale data. Check a store's `partialize` before persisting anything new.
- There is one deliberate cross-store call. `session.fetchSessionDetails` loads the whole session tree in one request, keeps the session metadata, and hands the activities to the `activity` store. Pages that show activities read them from the `activity` store only.
- Hiding admin actions in the UI is cosmetic. The backend is the authority and enforces roles itself.

### Logging sets

- Saved sets are read-only on the logging page. They can only be changed afterward through Edit Activity.
- Only the unsaved trailing set can be removed while logging.
- Exercises flagged `isBodyweight` fill the set weight from the user's profile weight and lock the weight field (reps stay editable). The number is copied into `activityset.weight` when the set is created, so it is a snapshot: changing the profile weight later never rewrites past sets. Without a profile weight the field stays editable and links to the profile page.

### Design system

Visual rules live in `docs/DESIGN_PHILOSOPHY.md`. The Chakra theme tokens (tiber, lime, mist) and fonts are defined in `theme.js`.

---

## Backend

Technology:

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- JPA/Hibernate
- Swagger/OpenAPI

Responsibilities:

- Authentication
- Authorization
- Business logic
- Session management
- Database interaction
- Integration with ExerciseDB

Backend deployment:

- AWS Elastic Beanstalk

### Structure

Requests flow Controller -> Service (interface plus implementation) -> Repository -> Entity. DTOs are grouped per feature (`DTO.Exercise`, `DTO.Profile`, `DTO.Activity`, `DTO.User`).

- Successful responses use `ApiSuccessResponse` (`status`, `message`, `data`).
- Errors are thrown as `BadRequestException(statusCode, message, details)` and turned into `ApiErrorResponse` (`errorCode`, `errorMessage`, `errorDetails`) by `GlobalExceptionHandler`. It maps the code to an HTTP status (400, 401, 403, 404, 409, 429, 500, 502) and also returns 400 for bean validation failures and malformed request bodies.
- Admin-only endpoints use `@PreAuthorize("hasRole('ADMIN')")`. Method security is enabled in `SecurityConfiguration`.
- Security is stateless. `/auth/**` and the Swagger paths are public, everything else requires a valid JWT.

### Main endpoints

Swagger UI is the source of truth. The main endpoints are:

| Area                | Endpoints                                                                                                                                                                                   |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Auth                | `POST /auth/signup`, `POST /auth/login`                                                                                                                                                     |
| Sessions            | `POST /insertSession`, `GET /session/active`, `PATCH /finishSession/{id}`, `DELETE /deleteSession/{id}`, `GET /getUserSessions`, `GET /sessions/{id}/details`                               |
| Activities and sets | `POST /insertActivity`, `DELETE /deleteActivity/{id}`, `DELETE /deleteActivitiesBySessionID/{id}`, `POST /insertSet`, `PUT /editActivitySet`                                                |
| Exercises           | `GET /getAllExercises`, `GET /getExercise/{id}`, `POST /insertExercise` (admin), `PUT /updateExercise` (admin), `DELETE /deleteExercise/{id}` (admin), `PUT /linkExerciseToDb/{id}` (admin) |
| ExerciseDB          | `GET /getExerciseDetails/{id}`, `GET /searchExerciseDbMatches` (admin)                                                                                                                      |
| Profile             | `GET /getProfile`, `PUT /updateProfile`, `DELETE /deleteAccount`                                                                                                                            |

---

## Database

Technology:

- MySQL
- AWS RDS

Main tables:

- users
- user_profile
- session
- activity
- activityset
- exercise
- ascend_api_usage

```text
users --1:1-- user_profile      shared primary key (user_profile.user_id)
users --1:N-- session           session.user_id
session --1:N-- activity        activity.sessionid
activity --1:N-- activityset    activityset.activityid
activity --N:1-- exercise       activity.exerciseID (plain column, not a foreign key)
ascend_api_usage                a single row, id = 1
```

Table notes:

- `user_profile` has no row until the user saves something. A missing row is a normal state, and the API returns empty fields for it.
- `exercise` has `is_bodyweight` (not null), `ascend_exercise_id` (nullable, the ExerciseDB ID) and `equipment` (nullable enum name: BARBELL, DUMBBELL, KETTLEBELL, MACHINE, CABLE, RESISTANCE_BAND, BODYWEIGHT, OTHER).
- `ascend_api_usage` holds the monthly and hourly call counters for the ExerciseDB quota.

Deleting data:

- The foreign keys from `session`, `activity`, `activityset` and `user_profile` are `ON DELETE CASCADE` in the database. Deleting a user removes their profile, sessions, activities and sets in one statement.
- `activity.exerciseID` is a plain column. The service layer refuses to delete an exercise that any activity uses (409), so workout history cannot lose its exercise.

### Schema management

Hibernate runs with `ddl-auto=update`. It adds missing tables and columns, but it does not change existing constraints and it does not insert data. These are applied by hand on every environment (local, any other dev machine, RDS) and then verified:

- changes to foreign key delete rules
- the single seed row of `ascend_api_usage`

```sql
CREATE TABLE IF NOT EXISTS ascend_api_usage (
    id INT PRIMARY KEY,
    monthly_count INT NOT NULL DEFAULT 0,
    monthly_period_start DATE NOT NULL,
    hourly_count INT NOT NULL DEFAULT 0,
    hourly_period_start DATETIME NOT NULL
);

INSERT IGNORE INTO ascend_api_usage
    (id, monthly_count, monthly_period_start, hourly_count, hourly_period_start)
VALUES (1, 0, CURDATE(), 0, NOW());
```

Check the delete rules (every foreign key listed above should say `CASCADE`):

```sql
SELECT tc.table_name, tc.constraint_name, rc.delete_rule
FROM information_schema.referential_constraints rc
JOIN information_schema.table_constraints tc
  ON rc.constraint_name = tc.constraint_name
 AND rc.constraint_schema = tc.constraint_schema
WHERE tc.table_schema = DATABASE();
```

---

## Authentication Flow

1. User logs in using email/password
2. Backend validates credentials
3. Backend returns JWT token
4. Frontend stores token using Zustand persist
5. Axios interceptor attaches JWT to requests
6. Backend validates JWT for protected endpoints

Roles are `USER` and `ADMIN`. Deleting an account requires the password again. The backend loads the user on every request, so a token for a deleted account stops working immediately.

---

## ExerciseDB Integration

Exercise reference data (image, muscles, instructions) comes from ExerciseDB v2 by AscendAPI, accessed through RapidAPI on the free Hobby plan: about 200 exercises, 2,000 calls per month and 1,000 per hour.

### Rules that shape the design

- Only the ExerciseDB exercise ID is stored permanently (`exercise.ascend_exercise_id`). AscendAPI support confirmed this in writing. Any other data from the API may not be kept longer than one hour.
- Matching is manual. An admin searches ExerciseDB from Edit Exercise and picks the right result, which avoids the wrong matches fuzzy name matching would produce. The link is saved by its own endpoint, so a normal edit cannot overwrite it.
- Enrichment is an extra. Every failure path ends in the existing placeholder UI, never in an error for a regular user.
- Attribution to ExerciseDB is required on the Hobby plan and is shown wherever its data appears.

### Request flow

```text
GET /getExerciseDetails/{exerciseID}
  no ascend_exercise_id on the exercise  -> { available: false }, no external call
  cache hit (55 minute TTL)              -> cached result
  cache miss -> usage limiter says no    -> { available: false }
             -> call ExerciseDB          -> result cached, including "not found"
  any exception                          -> { available: false }
```

### Components

| Component                    | Responsibility                                                                                                                                                 |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `AscendApiConfig`            | `RestTemplate` bean that adds the RapidAPI headers and sets 5 second connect and 10 second read timeouts                                                       |
| `AscendApiClient`            | Search, fetch by ID and liveness calls. Returns an empty `Optional` for "not found" and throws for real failures                                               |
| `ExerciseDetailsCache`       | In-memory cache, 55 minutes (under the provider's one hour limit). Caches confirmed misses. A lock per ExerciseDB ID makes concurrent requests share one fetch |
| `AscendApiRateLimiter`       | Counters in `ascend_api_usage`, updated under a row lock. Refuses calls at 1,800 per month or 900 per hour, below the real limits                              |
| `ExerciseDetailsServiceImpl` | Ties the pieces together and decides what each failure looks like                                                                                              |

Admin search (`GET /searchExerciseDbMatches`) uses the same quota and is not cached. It reports failures instead of hiding them: 429 when the safety limit is reached, 502 when ExerciseDB fails. An empty list therefore always means "no match".

On the frontend, `ExerciseDetailModal` and `CurrentActivityPage` ask for details only for linked exercises. The admin search in `LinkExerciseDbModal` runs only when the button is pressed, never while typing, because every search is one API call.

### Configuration

- `RAPIDAPI_KEY` environment variable (the key itself is never committed)
- `rapidapi.exercisedb.host` and `rapidapi.exercisedb.base-url` properties

---

## Testing

API tests are a Postman collection run by Newman from the repository root.

- `npm run test:api:local` runs the collection against the local environment file.
- `npm run test:api:prod` runs it against the production environment file. It creates and deletes real rows, so run it deliberately.
- `scripts/run-api-tests.js` reads the admin credentials from `.env.local` (gitignored) and injects them at run time, so no credentials sit in the committed collection or environment files.
- The collection covers authentication, sessions, activities and sets, profile validation, bodyweight exercises, ExerciseDB enrichment, cross-user access and account deletion.

---

## Deployment Architecture

Frontend:
User -> CloudFront -> S3

Backend:
User -> Elastic Beanstalk -> Spring Boot

Database:
Spring Boot -> RDS MySQL

Backend environment variables: `JWT_SECRET_KEY`, `RAPIDAPI_KEY` and the `RDS_*` database settings, all set in the Elastic Beanstalk environment.

Release order for changes that touch the schema: database, backend, frontend. Keep schema changes additive so the previous backend keeps working until the new one is live.

Run the backend on a single instance (see the limitations below).

---

## Known Limitations

- The ExerciseDB cache is per instance. With several backend instances each would keep its own cache. Quota tracking stays correct because the counters live in the database.
- The monthly counter resets on the calendar month. The provider's billing cycle may differ, and the margin between the safety limit (1,800) and the real limit (2,000) absorbs the difference.
- The Hobby plan covers about 200 exercises, so some library exercises will have no match and show the placeholder.
- `activity.exerciseID` is not a foreign key, so exercise integrity is enforced in the service layer rather than by the database.
- An exercise that has been used in a workout cannot be deleted, and there is no archive option yet.

---

## Future Improvements

- Google sign-in
- Analytics that use profile data (age from date of birth, gender, weight)
- Filtering the library by equipment and muscle group (muscle data would need to be admin-curated, because ExerciseDB data cannot be stored)
- Unlinking an exercise from ExerciseDB, and showing the overview text the API already returns
- Archiving exercises instead of deleting them
- A second data source or a paid ExerciseDB plan for exercises the free plan lacks
- CI that runs the Newman suite on every push
- A shared cache if the backend ever runs on more than one instance
