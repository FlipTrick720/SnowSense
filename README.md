# SnowSense (AI Generated)

Ski resort information system with real-time weather, avalanche warnings, and intelligent recommendations.

**🌐 Deployed on**: https://snowsense-j7cw.onrender.com/

## Tech Stack

- **Frontend**: Ionic 8.5 + React 19 + TypeScript
- **Backend**: Spring Boot 3.2.1 + Java 17
- **Database**: H2 (in-memory)
- **Scraping**: Playwright
- **Deployment**: Render (Docker)

## Quick Start

### Backend
```bash
cd snowsense/notification-backend
mvn spring-boot:run
```
Runs on http://localhost:8080

### Frontend
```bash
cd snowsense/app
npm install
npm run dev
```
Runs on http://localhost:5173

### Tests
```bash
# Backend tests
cd snowsense/notification-backend
mvn test

# Frontend tests
cd snowsense/app
npm test
```

## Key Features

### Core Features
✅ Real-time ski resort data (lifts, slopes, weather)
✅ Location-based resort recommendations
✅ Mobile-responsive UI
✅ SPA routing support

### Avalanche Safety
✅ Avalanche warnings with **elevation-specific hazards**
✅ **Hazard aspects** (which slopes are affected)
✅ **Avalanche problem types** (wind slab, persistent layers, etc.)
✅ Safety recommendations and key highlights
✅ Fixed elevation display for "treeline" values

### Backend
✅ Automated data updates
✅ Optimized for 512MB memory limit on Render
✅ Endpoints for all key data points

### Frontend
✅ Rich display of avalanche and weather data
✅ UI cards for elevation staffing, aspects, problems, and highlights
✅ Better visual hierarchy for critical safety information

## Data Collection Schedule

The backend automatically scrapes data from various sources at the following intervals:

- **Weather Data**: Every 30 minutes
- **Avalanche Reports**: Every hour
- **Ski Resort Infrastructure (Lifts & Slopes)**: Every hour

Manual scraping can also be triggered via API endpoints.

## API Endpoints

The backend provides a rich set of REST APIs to access the collected data. Here are some of the key endpoints:

- `GET /api/resorts/with-avalanche` - All resorts with avalanche data
- `GET /api/skiresort/lifts` - All lift statuses
- `GET /api/weather` - Weather data
- `POST /api/recommendation/skiresort` - Get recommendations
- `GET /api/health` - Health check

For a full list of endpoints and their usage, please refer to the [API Quick Reference](./API_QUICK_REFERENCE.md).

