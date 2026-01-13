# SnowSense (AI Generated)

Ski resort information system with real-time weather, avalanche warnings, and intelligent recommendations.

**🌐 Deployed on**: https://snowsense.onrender.com

## Tech Stack

- **Frontend**: Ionic 8.5 + React 19 + TypeScript
- **Backend**: Spring Boot 3.2.1 + Java 17
- **Database**: H2 (in-memory)
- **Scraping**: Playwright

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

- Real-time ski resort data (lifts, slopes, weather)
- Avalanche warnings and safety info
- Location-based resort recommendations
- Automated hourly data updates
- Mobile-responsive UI

## API Endpoints

- `GET /api/resorts/with-avalanche` - All resorts with avalanche data
- `GET /api/skiresort/lifts` - All lift statuses
- `GET /api/weather` - Weather data
- `POST /api/recommendation/skiresort` - Get recommendations

Full API docs: [API_QUICK_REFERENCE.md](./API_QUICK_REFERENCE.md)

