# API Quick Reference (AI Generated)

## 🌤️ Weather API

| Method | Endpoint | Description | Schedule |
|--------|----------|-------------|----------|
| GET | `/api/weather` | Get all weather data | - |
| GET | `/api/weather/resort/{id}` | Get weather for specific resort | - |
| POST | `/api/weather/scrape` | Trigger weather scrape | Auto: Every 5 min |

## 🏔️ Avalanche API

| Method | Endpoint | Description | Schedule |
|--------|----------|-------------|----------|
| GET | `/api/avalanche` | Get all bulletins | - |
| GET | `/api/avalanche/current` | Get currently valid bulletins | - |
| GET | `/api/avalanche/region/{code}` | Get bulletins by region code | - |
| GET | `/api/avalanche/high-danger` | Get high danger bulletins | - |
| POST | `/api/avalanche/scrape` | Trigger avalanche scrape | Auto: Every 5 min |

## 🎿 Ski Resort + Avalanche Combined API

| Method | Endpoint | Description | Use Case |
|--------|----------|-------------|----------|
| GET | `/api/resorts/with-avalanche` | All resorts with avalanche data | **Main endpoint for ML** |
| GET | `/api/resorts/{id}/with-avalanche` | Single resort with avalanche data | Resort detail page |
| GET | `/api/resorts/safe` | Only safe resorts (low/moderate) | Filter safe options |
| GET | `/api/resorts/high-danger` | Resorts with high danger warnings | Safety alerts |

## 🌡️ Conditions API (Not Implemented)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/conditions` | Get combined weather and avalanche conditions for all resorts |
| GET | `/api/conditions/{resortId}` | Get combined weather and avalanche conditions for a specific resort |
| POST | `/api/conditions/scrape` | Trigger scraping of both weather and avalanche data |

## 🚡 Ski Resort Infrastructure API

| Method | Endpoint | Description | Schedule |
|--------|----------|-------------|----------|
| GET | `/api/skiresort/lifts` | Get all lift data | - |
| GET | `/api/skiresort/resort/{id}/lifts` | Get lifts for specific resort | - |
| GET | `/api/skiresort/slopes` | Get all slope data | - |
| GET | `/api/skiresort/resort/{id}/slopes` | Get slopes for specific resort | - |
| GET | `/api/skiresort/scrape` | Trigger infrastructure scrape | Auto: Every hour |

## 🔔 Notification API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/notifications` | Create/send notification |
| GET | `/api/notifications` | Get all notifications |
| POST | `/api/notifications/subscribe` | Subscribe device token |
| POST | `/api/notifications/unsubscribe` | Unsubscribe device token |

## Recommendation API

| Method | Endpoint | Description | Default |
|--------|----------|-------------|---------|
| GET | `/api/recommendation/skiresort` | Get best resort by temperature | Target: -4°C |

**Returns:** Resort with temperature closest to `targetTemp` (default: -4)
Example for -2 C°: `GET http://localhost:8080/api/recommendation/skiresort?targetTemp=-2`

## 🏥 Health Check

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Health check endpoint |


## Data Collection Schedule

- **Weather**: Every 5 minutes (automatic)
- **Avalanche**: Every 5 minutes (automatic)
- **Ski Resort Infrastructure**: Every hour (automatic)

## Database Access

H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:snowsense`
- Username: `sa`
- Password: (empty)

