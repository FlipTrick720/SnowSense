# SnowSense (AI Generated)

Ski resort information system with real-time weather, avalanche warnings, and intelligent recommendations.

**🌐 Deployed on**: https://snowsense.onrender.com

## Tech Stack

- **Frontend**: Ionic 8.5 + React 19 + TypeScript
- **Backend**: Spring Boot 3.2.1 + Java 17
- **Database**: H2 (in-memory)
- **Scraping**: Playwright
- **Deployment**: Render (Docker)

## Quick Start

### Backend
```bash
cd notification-backend
mvn spring-boot:run
```
Runs on http://localhost:8080

### Frontend
```bash
cd app
npm install
npm run dev
```
Runs on http://localhost:5173

### Tests
```bash
mvn test              # Backend + fitness functions
npm test              # Frontend
```

## Key Features

✅ Real-time ski resort data (lifts, slopes, weather)
✅ Avalanche warnings with **elevation-specific hazards**
✅ **Hazard aspects** (which slopes are affected)
✅ **Avalanche problem types** (wind slab, persistent layers, etc.)
✅ Safety recommendations and key highlights
✅ Location-based resort recommendations
✅ Automated hourly data updates
✅ Mobile-responsive UI
✅ SPA routing support

## API Endpoints

- `GET /api/resorts/with-avalanche` - All resorts with avalanche data
- `GET /api/skiresort/lifts` - All lift statuses
- `GET /api/weather` - Weather data
- `POST /api/recommendation/skiresort` - Get recommendations
- `GET /api/health` - Health check

Full API docs: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md)

## Recent Updates (Jan 2026)

### Security & Safety Enhancements
- Extended avalanche data display with **elevation bounds**
- Added **affected aspects** (N, NE, E, SE, S, SW, W, NW)
- Display **avalanche problem types** with descriptions
- Show **safety recommendations** and key highlights
- Fixed elevation display for "treeline" values

### Backend Optimizations (Render Deployment)
- Fixed SPA routing for direct URL access (`/app/home` now works)
- Reduced scheduled tasks from 5-second to 60-minute intervals
- Disabled raw JSON storage to save memory (~50-80% reduction)
- Optimized database connection pool for 512MB memory limit
- Added proper error page mapping (`/error` endpoint)
- Reduced logging verbosity for production

### Frontend Improvements
- Extended `ResortDetail` interface with all safety-critical fields
- New UI cards for elevation staffing, aspects, problems, and highlights
- Better visual hierarchy for critical safety information

