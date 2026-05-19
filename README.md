# ManaProgressAku 🏋️

**ManaProgressAku** is a full-stack fitness progress tracking application designed to help users record, manage, and review their workout sessions in a clean and intuitive way.

The project was built to simulate a real-world production application — covering not only frontend and backend development, but also authentication, deployment, cloud infrastructure, state management, API communication, and documentation.

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

## Workout Tracking

- Create workout sessions
- Add exercises to a session
- Record sets, reps, and weights
- View past workout sessions
- Dynamic workout summaries

## User Experience

- Responsive modern UI
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

## Backend

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- JPA / Hibernate

## Database

- MySQL
- Amazon RDS

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

# 📚 Documentation

This repository also contains additional technical documentation:

| Document               | Purpose                                                    |
| ---------------------- | ---------------------------------------------------------- |
| [docs/ARCHITECTURE.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/ARCHITECTURE.md) | High-level system architecture and infrastructure overview |
| [docs/CHANGELOG.md](https://github.com/Kuasawan-Murbawan/ManaProgressAku/blob/master/docs/CHANGELOG.md)    | Project release history and planned improvements           |
| Swagger UI             | Detailed API endpoint documentation                        |

---

# 🧪 Future Improvements

Planned features for future releases include:

- Edit workout activities
- Active session recovery
- Better analytics & workout insights
- Automated smoke testing
- CI/CD improvements
- Enhanced mobile responsiveness

---

# 🏗️ Local Development Setup

## Frontend

```bash
cd Frontend
npm install
npm run dev
```

## Backend

```bash
cd Backend
mvn spring-boot:run
```
