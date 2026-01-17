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

# Expose port (Hugging Face default is 7860)
# The application will bind to the PORT environment variable
EXPOSE 7860

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check: curl /api/health endpoint
# This ensures the container is marked as healthy immediately when Spring Boot starts
# It dynamically uses the PORT variable provided by the environment (e.g., Hugging Face)
HEALTHCHECK --interval=10s --timeout=5s --start-period=15s --retries=3 \
    CMD sh -c 'curl -f http://localhost:${PORT:-7860}/api/health || exit 1'

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
