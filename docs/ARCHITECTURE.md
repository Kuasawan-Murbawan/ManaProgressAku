# ManaProgressAku Architecture

## Overview

ManaProgressAku is a full-stack fitness tracking application that allows users to:

- register/login
- create workout sessions
- add exercises
- track sets and reps
- review workout history

The application follows a client-server architecture.

---

## Frontend

Technology:

- React
- Vite
- Chakra UI
- Zustand
- Axios

Responsibilities:

- Render UI
- Handle authentication state
- Manage routing
- Communicate with backend APIs

Frontend deployment:

- AWS S3
- CloudFront CDN

---

## Backend

Technology:

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- JPA/Hibernate

Responsibilities:

- Authentication
- Authorization
- Business logic
- Session management
- Database interaction

Backend deployment:

- AWS Elastic Beanstalk

---

## Database

Technology:

- MySQL
- AWS RDS

Main tables:

- user
- session
- activity
- exercise

Responsibilities:

- Persist user data
- Store workout sessions
- Store activity history

---

## Authentication Flow

1. User logs in using email/password
2. Backend validates credentials
3. Backend returns JWT token
4. Frontend stores token using Zustand persist
5. Axios interceptor attaches JWT to requests
6. Backend validates JWT for protected endpoints

---

## Deployment Architecture

Frontend:
User → CloudFront → S3

Backend:
User → Elastic Beanstalk → Spring Boot

Database:
Spring Boot → RDS MySQL

---

## Future Improvements

Planned for v1.1.0:

- Active session recovery
- Edit exercise functionality
- Better analytics
- Automated testing
