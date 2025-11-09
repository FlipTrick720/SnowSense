#!/bin/bash
set -e

echo "🔨 Building React frontend..."
cd notification-frontend
npm install
npm run build
cd ..

echo "📦 Copying frontend build to Spring Boot static resources..."
rm -rf notification-backend/src/main/resources/static/*
mkdir -p notification-backend/src/main/resources/static
cp -r notification-frontend/dist/* notification-backend/src/main/resources/static/

echo "🔨 Building Spring Boot backend..."
cd notification-backend
mvn clean package -DskipTests
cd ..

echo "✅ Build complete! JAR file is at: notification-backend/target/*.jar"
echo "🚀 You can now run: java -jar notification-backend/target/*.jar"
