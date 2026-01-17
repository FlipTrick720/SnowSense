# API Quick Reference (AI Generated)

This document provides a summary of the available API endpoints.

## Data Collection Schedule

- **Weather Data**: Every 30 minutes
- **Avalanche Reports**: Every hour
- **Ski Resort Infrastructure (Lifts & Slopes)**: Every hour

Manual scraping can be triggered via the `POST /api/.../scrape` endpoints.

---

## Main APIs

### Ski Resort & Avalanche Data
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/resorts/with-avalanche` | Get all resorts with current avalanche data. **Main endpoint for the frontend.** |
| GET | `/api/resorts/{id}/with-avalanche` | Get a single resort with avalanche data. |
| GET | `/api/resorts/safe` | Get resorts that are currently considered safe (low/moderate danger). |
| GET | `/api/resorts/high-danger` | Get resorts with high danger warnings. |

### Recommendation
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/recommendation/skiresort` | Recommends ski resorts based on user location. Expects a JSON body with `latitude` and `longitude`. |
| GET | `/api/recommendation/skiresort` | Recommends ski resorts based on user location. Expects `latitude` and `longitude` as query parameters. |

### Infrastructure (Lifts & Slopes)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/skiresort/lifts` | Get all lift statuses. |
| GET | `/api/skiresort/slopes` | Get all slope statuses. |
| GET | `/api/skiresort/resort/{id}/lifts` | Get lifts for a specific resort. |
| GET | `/api/skiresort/resort/{id}/slopes` | Get slopes for a specific resort. |

### Weather Data
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weather` | Get all weather data. |
| GET | `/api/weather/resort/{id}` | Get weather for a specific resort. |

### Avalanche Data
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/avalanche` | Get all avalanche bulletins. |
| GET | `/api/avalanche/current` | Get currently valid bulletins. |
| GET | `/api/avalanche/region/{code}` | Get bulletins by region code (e.g., `AT-07-14`). |
| GET | `/api/avalanche/high-danger` | Get bulletins with high danger warnings. |

---

## Notification API

The notification system allows sending push notifications to subscribed users.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/notifications/subscribe` | Subscribes a device to receive push notifications. |
| POST | `/api/notifications/unsubscribe` | Unsubscribes a device. |
| POST | `/api/notifications` | Creates and sends a notification to **all** subscribed devices. |
| GET | `/api/notifications` | Get a list of all notifications sent. |

**Important:** The current implementation broadcasts every created notification to all subscribed users. There is no user-specific targeting.

---

## Manual Scraping APIs

These endpoints are useful for development and for forcing data updates.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/conditions/scrape` | Triggers a full scrape of all data sources (avalanche, weather, and ski resort infrastructure). This can take a long time. |
| POST | `/api/avalanche/scrape` | Triggers a scrape of avalanche data only. |
| POST | `/api/weather/scrape` | Triggers a scrape of weather data only. |
| GET | `/api/skiresort/scrape` | Triggers a scrape of ski resort infrastructure only. |

---

## Utility APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Health check endpoint for deployment platforms. Returns `{"status": "UP"}`. |

---

## Database Access

For local development, you can access the H2 in-memory database console:

- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:snowsense`
- **Username**: `sa`
- **Password**: (leave empty)

