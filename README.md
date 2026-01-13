# SnowSense - Ski Resort Notification & Recommendation System

A full-stack ski resort information system with weather data, avalanche warnings, and intelligent recommendations. Built with Ionic/React (frontend) and Spring Boot (backend).

## Features

- ✅ Real-time ski resort data (lifts, slopes, weather)
- ✅ Avalanche warnings and safety information
- ✅ Intelligent resort recommendations based on location
- ✅ Web scraping for live resort status
- ✅ Responsive mobile-first UI with Ionic
- ✅ RESTful API backend
- ✅ Automated data updates

## Tech Stack

**Frontend:**
- Ionic 8.5
- React 19
- TypeScript
- Vite
- Capacitor (for mobile)

**Backend:**
- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- Playwright (web scraping)
- Maven

## 🚀 Quick Start Commands

### Backend
```bash
cd notification-backend
mvn spring-boot:run
```
Backend runs on: http://localhost:8080

### Frontend
```bash
cd app
npm install
npm run dev
```
Frontend runs on: http://localhost:5173

### Run Tests
```bash
# Backend tests (includes fitness function)
cd notification-backend
mvn test

# Frontend tests
cd app
npm test
```

## Prerequisites

- Node.js 18+ and npm
- Java 17+
- Maven 3.6+

## Detailed Setup

### 1. Clone Repository

```bash
git clone <your-repo-url>
cd snowsense
```

### 2. Backend Setup

```bash
cd notification-backend
mvn clean install
mvn spring-boot:run
```

The backend will:
- Start on port 8080
- Initialize H2 database
- Begin scraping ski resort data
- Fetch weather and avalanche information

### 3. Frontend Setup

```bash
cd app
npm install
npm run dev
```

The frontend will:
- Start on port 5173
- Connect to backend API
- Enable geolocation for recommendations

## Project Structure

```
.
├── app/                           # Ionic/React frontend
│   ├── src/
│   │   ├── components/           # React components
│   │   ├── pages/                # Page components
│   │   ├── context/              # React context (data management)
│   │   └── theme/                # Ionic theming
│   └── package.json
│
├── notification-backend/          # Spring Boot backend
│   ├── src/main/java/com/notification/
│   │   ├── controller/           # REST controllers
│   │   ├── service/              # Business logic
│   │   ├── repository/           # Data access
│   │   ├── model/                # Domain models
│   │   └── dto/                  # Data transfer objects
│   ├── src/test/java/
│   │   └── fitness/              # Architecture fitness functions
│   └── pom.xml
│
├── Dockerfile                     # Multi-stage Docker build
├── render.yaml                    # Render deployment config
└── README.md
```

## API Endpoints

### Ski Resorts
- `GET /api/skiresort` - Get all ski resorts
- `GET /api/skiresort/{id}` - Get resort by ID
- `GET /api/skiresort/lifts` - Get all lift statuses
- `GET /api/skiresort/slopes` - Get all slope statuses
- `GET /api/skiresort/scrape` - Trigger data scraping

### Weather
- `GET /api/weather` - Get all weather data
- `GET /api/weather/resort/{id}` - Get weather for specific resort

### Avalanche
- `GET /api/avalanche` - Get all avalanche warnings
- `GET /api/avalanche/current` - Get current valid warnings

### Recommendations
- `POST /api/recommendation/skiresort` - Get personalized recommendations
  ```json
  {
    "latitude": 47.2692,
    "longitude": 11.4041
  }
  ```

### Combined Data
- `GET /api/resorts/with-avalanche` - Get resorts with avalanche data
- `GET /api/resorts/{id}/with-avalanche` - Get specific resort with avalanche data

## Development

### Backend Development

```bash
cd notification-backend

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Run fitness functions
mvn test -Dtest=ServiceCouplingFitnessFunctionTest

# Build JAR
mvn clean package

# Clean build
mvn clean install
```

### Frontend Development

```bash
cd app

# Development server
npm run dev

# Build for production
npm run build

# Run tests
npm test

# Lint code
npm run lint

# Preview production build
npm run preview
```

## Architecture Fitness Functions

This project implements fitness functions to maintain architectural quality:

**Service Layer Coupling Fitness Function**
- Ensures services don't exceed 3 dependencies
- Protects modifiability characteristic
- Runs automatically with `mvn test`

See `notification-backend/FITNESS_FUNCTION_README.md` for details.

## Deployment

### Production (Render)

The application is deployed at: **https://snowsense.onrender.com**

Deployment is automatic from the `frontend` branch:
1. Push to `frontend` branch
2. Render builds Docker image
3. Deploys to production

### Docker Build

```bash
# Build image
docker build -t snowsense .

# Run container
docker run -p 8080:8080 snowsense
```

## Environment Variables

### Frontend (Build-time)
- `VITE_API_URL` - Backend API URL (defaults to production)

### Backend (Runtime)
- `SERVER_PORT` - Server port (default: 8080)
- `CORS_ALLOWED_ORIGINS` - Allowed CORS origins

## Data Sources

- **Weather**: OpenMeteo API
- **Avalanche**: Austrian Avalanche Warning Service (CAAML format)
- **Ski Resorts**: Bergfex.at (web scraping)

## Automated Tasks

- Weather data: Updates every 5 minutes
- Avalanche warnings: Updates daily at 8 AM
- Ski resort status: Updates hourly

## Browser Support

- ✅ Chrome/Edge (Desktop & Mobile)
- ✅ Firefox (Desktop & Mobile)
- ✅ Safari (Desktop & Mobile)
- ✅ Mobile apps via Capacitor

## Troubleshooting

### Backend won't start?
- Check Java version: `java -version` (requires 17+)
- Check Maven: `mvn -version`
- Clean and rebuild: `mvn clean install`

### Frontend won't start?
- Check Node version: `node -version` (requires 18+)
- Clear node_modules: `rm -rf node_modules && npm install`
- Check port 5173 is available

### CORS errors?
- Backend must be running on port 8080
- Frontend uses `VITE_API_URL` or defaults to production

### Playwright errors?
- System dependencies are installed via Dockerfile
- For local development, Playwright installs automatically

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests: `mvn test` and `npm test`
5. Submit a pull request

## License

MIT License

---

**Live Demo**: https://snowsense.onrender.com
**Need help?** Open an issue on the repository!
