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
echo "$RESPONSE" | jq -r '.message' | xargs echo "   "

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

# Combined API Tests
echo "🎿 COMBINED CONDITIONS API"
echo "-----------------------------------------"

echo "1. GET /api/conditions (all conditions)"
RESPONSE=$(curl -s "$BASE_URL/api/conditions")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo "2. GET /api/conditions/1 (specific resort)"
RESPONSE=$(curl -s "$BASE_URL/api/conditions/1")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo "3. POST /api/conditions/scrape (trigger combined scrape)"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/conditions/scrape")
echo "   Response: $(echo "$RESPONSE" | cut -c1-50)..."
echo "$RESPONSE" | jq -r '.status' | xargs echo "   Status:"

echo ""
echo ""
echo "========================================="
echo "✅ API Testing Complete"
echo "========================================="
echo ""
echo "📝 Notes:"
echo "   - Weather data updates every 15 minutes"
echo "   - Avalanche data updates daily at 8:00 AM"
echo "   - Conditions API endpoints are placeholders"
echo "   - /api/resort is available for resort data scraping"
echo ""
