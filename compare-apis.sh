#!/bin/bash

# A script to curl the SnowSense APIs for local and deployed environments to compare their output.

# --- Configuration ---
LOCAL_API_BASE_URL="http://localhost:8080/api"
RENDER_API_BASE_URL="https://snowsense.onrender.com/api"
# We'll try to get a resort ID from the API, but have a fallback.
RESORT_ID=1

# --- Output Directories ---
mkdir -p z_local_output
mkdir -p z_render_output

# --- Helper Functions ---
function print_header() {
    echo "=========================================================="
    echo "$1"
    echo "=========================================================="
}

function get_resort_id() {
    local url="$1"
    echo "Fetching resorts from $url to get a resort ID..."
    local response=$(curl -s "$url/resorts/with-avalanche")
    
    # Try to extract the first resort ID from the JSON response
    local id=$(echo "$response" | grep -o '"id":\s*[0-9]*' | head -1 | grep -o '[0-9]*')
    
    if [[ -n "$id" ]]; then
        RESORT_ID=$id
        echo "Found resort ID: $RESORT_ID"
    else
        echo "Could not find a resort ID from the API. Using fallback ID: $RESORT_ID"
    fi
}

# --- Main Execution ---

print_header "Fetching data from LOCAL API ($LOCAL_API_BASE_URL)"
get_resort_id "$LOCAL_API_BASE_URL"

echo "Curling /resorts/with-avalanche..."
curl -s "$LOCAL_API_BASE_URL/resorts/with-avalanche" > z_local_output/resorts_with_avalanche.json

echo "Curling /skiresort/lifts..."
curl -s "$LOCAL_API_BASE_URL/skiresort/lifts" > z_local_output/all_lifts.json

echo "Curling /skiresort/slopes..."
curl -s "$LOCAL_API_BASE_URL/skiresort/slopes" > z_local_output/all_slopes.json

echo "Curling /skiresort/resort/$RESORT_ID/lifts..."
curl -s "$LOCAL_API_BASE_URL/skiresort/resort/$RESORT_ID/lifts" > "z_local_output/resort_${RESORT_ID}_lifts.json"

echo "Curling /skiresort/resort/$RESORT_ID/slopes..."
curl -s "$LOCAL_API_BASE_URL/skiresort/resort/$RESORT_ID/slopes" > "z_local_output/resort_${RESORT_ID}_slopes.json"


print_header "Fetching data from RENDER API ($RENDER_API_BASE_URL)"
get_resort_id "$RENDER_API_BASE_URL"

echo "Curling /resorts/with-avalanche..."
curl -s "$RENDER_API_BASE_URL/resorts/with-avalanche" > z_render_output/resorts_with_avalanche.json

echo "Curling /skiresort/lifts..."
curl -s "$RENDER_API_BASE_URL/skiresort/lifts" > z_render_output/all_lifts.json

echo "Curling /skiresort/slopes..."
curl -s "$RENDER_API_BASE_URL/skiresort/slopes" > z_render_output/all_slopes.json

echo "Curling /skiresort/resort/$RESORT_ID/lifts..."
curl -s "$RENDER_API_BASE_URL/skiresort/resort/$RESORT_ID/lifts" > "z_render_output/resort_${RESORT_ID}_lifts.json"

echo "Curling /skiresort/resort/$RESORT_ID/slopes..."
curl -s "$RENDER_API_BASE_URL/skiresort/resort/$RESORT_ID/slopes" > "z_render_output/resort_${RESORT_ID}_slopes.json"


print_header "Done."
echo "Check the 'z_local_output' and 'z_render_output' directories for the JSON responses."
echo "You can use a diff tool to compare the files, for example:"
echo "diff z_local_output/all_slopes.json z_render_output/all_slopes.json"
echo "diff z_local_output/all_lifts.json z_render_output/all_lifts.json"
echo "diff z_local_output/resort_${RESORT_ID}_slopes.json z_render_output/resort_${RESORT_ID}_slopes.json"

