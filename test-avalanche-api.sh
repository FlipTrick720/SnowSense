#!/bin/bash
#(AI Generated)

echo "🏔️  Testing Avalanche Data API"
echo "================================"
echo ""

BASE_URL="http://localhost:8080"

echo "1️⃣  Triggering avalanche data scrape..."
curl -s -X POST "$BASE_URL/api/avalanche/scrape"
echo ""
echo ""
sleep 2

echo "2️⃣  Getting all avalanche bulletins..."
curl -s "$BASE_URL/api/avalanche" | jq '.[0:2]'
echo ""
echo ""

echo "3️⃣  Getting currently valid bulletins..."
curl -s "$BASE_URL/api/avalanche/current" | jq '.[0:2]'
echo ""
echo ""

echo "4️⃣  Getting high danger bulletins..."
curl -s "$BASE_URL/api/avalanche/high-danger" | jq
echo ""
echo ""

echo "5️⃣  Getting bulletins for specific region (AT-07-14 - Stubai Alps)..."
curl -s "$BASE_URL/api/avalanche/region/AT-07-14" | jq
echo ""
echo ""

echo "✅ Test complete!"
echo ""
echo "💡 Tip: Install jq for better JSON formatting: sudo apt install jq"
