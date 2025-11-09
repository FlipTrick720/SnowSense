#!/bin/bash
set -e

# Load environment variables from .env file
if [ -f notification-backend/.env ]; then
    echo "📋 Loading environment variables from notification-backend/.env"
    export $(cat notification-backend/.env | grep -v '^#' | xargs)
fi

# Update CORS to allow localhost:8080 (since everything runs on same port now)
export CORS_ALLOWED_ORIGINS="http://localhost:8080"

echo "🚀 Starting packaged application..."
echo "   Firebase: ${FIREBASE_SERVICE_ACCOUNT_PATH}"
echo "   Port: ${SERVER_PORT}"
echo "   CORS: ${CORS_ALLOWED_ORIGINS}"
echo ""
echo "📱 Open http://localhost:8080 in your browser"
echo ""

java -jar notification-backend/target/notification-backend-1.0.0.jar
