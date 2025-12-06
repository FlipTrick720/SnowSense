#!/bin/bash

BASE_URL="http://localhost:8080"

echo "========================================="
echo "🧪 Testing All APIs"
echo "========================================="
echo ""

# Weather API Tests
echo "📊 WEATHER API"
echo "-----------------------------------------"

echo "1. GET /api/weather (all weather data)"
RESPONSE=$(curl -s "$BASE_URL/api/weather")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Records found:"

echo ""
echo "2. POST /api/weather/scrape (trigger scrape)"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/weather/scrape")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.message' | xargs echo "   "

echo ""
echo "3. GET /api/weather/resort/1 (specific resort)"
RESPONSE=$(curl -s "$BASE_URL/api/weather/resort/1")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Records found:"

echo ""
echo ""

# Avalanche API Tests
echo "🏔️  AVALANCHE API"
echo "-----------------------------------------"

echo "1. GET /api/avalanche (all bulletins)"
RESPONSE=$(curl -s "$BASE_URL/api/avalanche")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Bulletins found:"

echo ""
echo "2. POST /api/avalanche/scrape (trigger scrape)"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/avalanche/scrape")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."

echo ""
echo "3. GET /api/avalanche/current (valid bulletins)"
RESPONSE=$(curl -s "$BASE_URL/api/avalanche/current")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Valid bulletins:"

echo ""
echo "4. GET /api/avalanche/region/AT-07-14 (by region)"
RESPONSE=$(curl -s "$BASE_URL/api/avalanche/region/AT-07-14")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Bulletins for AT-07-14:"

echo ""
echo "5. GET /api/avalanche/high-danger (high danger)"
RESPONSE=$(curl -s "$BASE_URL/api/avalanche/high-danger")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   High danger bulletins:"

echo ""
echo ""

# Ski Resort + Avalanche Combined API Tests
echo "🎿 SKI RESORT + AVALANCHE COMBINED API"
echo "-----------------------------------------"

echo "1. GET /api/resorts/with-avalanche (all resorts with avalanche data)"
RESPONSE=$(curl -s "$BASE_URL/api/resorts/with-avalanche")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Resorts found:"

echo ""
echo "2. GET /api/resorts/1/with-avalanche (specific resort)"
RESPONSE=$(curl -s "$BASE_URL/api/resorts/1/with-avalanche")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.resortName' | xargs echo "   Resort:"
echo "$RESPONSE" | jq -r '.safetyStatus' | xargs echo "   Safety Status:"

echo ""
echo "3. GET /api/resorts/safe (safe resorts only)"
RESPONSE=$(curl -s "$BASE_URL/api/resorts/safe")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Safe resorts:"

echo ""
echo "4. GET /api/resorts/high-danger (high danger resorts)"
RESPONSE=$(curl -s "$BASE_URL/api/resorts/high-danger")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   High danger resorts:"

echo ""
echo ""

# Ski Resort Infrastructure API Tests
echo "🚡 SKI RESORT INFRASTRUCTURE API"
echo "-----------------------------------------"

echo "1. GET /api/skiresort/lifts (all lifts)"
RESPONSE=$(curl -s "$BASE_URL/api/skiresort/lifts")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Lifts found:"

echo ""
echo "2. GET /api/skiresort/slopes (all slopes)"
RESPONSE=$(curl -s "$BASE_URL/api/skiresort/slopes")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Slopes found:"

echo ""
echo "3. GET /api/skiresort/resort/1/lifts (lifts for resort 1)"
RESPONSE=$(curl -s "$BASE_URL/api/skiresort/resort/1/lifts")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Lifts for resort:"

echo ""
echo "4. GET /api/skiresort/resort/1/slopes (slopes for resort 1)"
RESPONSE=$(curl -s "$BASE_URL/api/skiresort/resort/1/slopes")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Slopes for resort:"

echo ""
echo "5. GET /api/skiresort/scrape (trigger infrastructure scrape)"
RESPONSE=$(curl -s "$BASE_URL/api/skiresort/scrape")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo ""

# Notification API Tests
echo "🔔 NOTIFICATION API"
echo "-----------------------------------------"

echo "1. GET /api/notifications (all notifications)"
RESPONSE=$(curl -s "$BASE_URL/api/notifications")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq 'length' | xargs echo "   Notifications found:"

echo ""
echo "2. POST /api/notifications (create notification)"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/notifications" \
  -H "Content-Type: application/json" \
  -d '{"token":"test-token","title":"Test","body":"Test notification"}')
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.title' | xargs echo "   Title:"

echo ""
echo "3. POST /api/notifications/subscribe (subscribe device)"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/notifications/subscribe" \
  -H "Content-Type: application/json" \
  -d '{"token":"test-device-token"}')
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo "4. POST /api/notifications/unsubscribe (unsubscribe device)"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/notifications/unsubscribe" \
  -H "Content-Type: application/json" \
  -d '{"token":"test-device-token"}')
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo ""

# Health Check
echo "🏥 HEALTH CHECK"
echo "-----------------------------------------"

echo "1. GET /api/health (health check)"
RESPONSE=$(curl -s "$BASE_URL/api/health")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo ""
echo "========================================="
echo "✅ API Testing Complete"
echo "========================================="
echo ""
echo "📝 Notes:"
echo "   - Weather data updates every 5 minutes (automatic)"
echo "   - Avalanche data updates daily at 8:00 AM (automatic)"
echo "   - Ski resort infrastructure updates every hour (automatic)"
echo "   - Main endpoint for ML: /api/resorts/with-avalanche"
echo ""
