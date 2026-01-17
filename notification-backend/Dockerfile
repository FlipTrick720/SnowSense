# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
# Use official Playwright Java image with all browser dependencies pre-installed
# This avoids downloading browsers at runtime and fixes missing dependencies
FROM mcr.microsoft.com/playwright/java:v1.41.0-jammy

# Install curl for health checks
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/notification-backend-1.0.0.jar app.jar

# FIX: Hugging Face requires port 7860
EXPOSE 7860

# FIX: Update ENV variable
ENV SERVER_PORT=7860
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# FIX: Update Healthcheck to use port 7860
HEALTHCHECK --interval=10s --timeout=5s --start-period=15s --retries=3 \
    CMD curl -f http://localhost:7860/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
