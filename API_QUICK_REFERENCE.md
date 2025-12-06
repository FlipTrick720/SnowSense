# API Quick Reference (AI Generated)

## 🌤️ Weather API

| Method | Endpoint | Description | Schedule |
|--------|----------|-------------|----------|
| GET | `/api/weather` | Get all weather data | - |
| GET | `/api/weather/resort/{id}` | Get weather for specific resort | - |
| POST | `/api/weather/scrape` | Trigger weather scrape | Auto: Every 15 min |

## 🏔️ Avalanche API

| Method | Endpoint | Description | Schedule |
|--------|----------|-------------|----------|
| GET | `/api/avalanche` | Get all bulletins | - |
| GET | `/api/avalanche/current` | Get currently valid bulletins | - |
| GET | `/api/avalanche/region/{code}` | Get bulletins by region | - |
| GET | `/api/avalanche/high-danger` | Get high danger bulletins | - |
| POST | `/api/avalanche/scrape` | Trigger avalanche scrape | Auto: Daily 8 AM |

## 🎿 Combined Conditions API (Not Implemented)

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/conditions` | Get all conditions | Placeholder |
| GET | `/api/conditions/{id}` | Get specific resort conditions | Placeholder |
| POST | `/api/conditions/scrape` | Trigger combined scrape | Placeholder |

## Quick Examples

```bash
# Get weather data
curl http://localhost:8080/api/weather

# Trigger weather scrape
curl -X POST http://localhost:8080/api/weather/scrape

# Get current avalanche warnings
curl http://localhost:8080/api/avalanche/current

# Trigger avalanche scrape
curl -X POST http://localhost:8080/api/avalanche/scrape

# Get bulletins for Stubai Alps
curl http://localhost:8080/api/avalanche/region/AT-07-14

# Test all APIs
./test-all-apis.sh
```

## Data Collection Schedule

- **Weather**: Every 5 minutes (automatic)
- **Avalanche**: Every 5 minutes (automatic)

