# Multi-stage build for Spring Boot + React app
FROM node:20-alpine AS frontend-build

WORKDIR /app/frontend
COPY notification-frontend/package*.json ./
RUN npm ci
COPY notification-frontend/ ./
RUN npm run build

# Backend build stage
FROM maven:3.9-eclipse-temurin-17 AS backend-build

WORKDIR /app
COPY notification-backend/pom.xml ./
RUN mvn dependency:go-offline

COPY notification-backend/src ./src
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
