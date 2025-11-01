# Notification System with Push Notifications (AI Generated)

A full-stack notification system with Firebase Cloud Messaging (FCM) push notifications. Built with React (frontend) and Spring Boot (backend).

## Features

- ✅ Create and view notifications
- ✅ Real-time push notifications (even when browser is closed!)
- ✅ Firebase Cloud Messaging integration
- ✅ Service worker for background notifications
- ✅ Responsive UI with modern design
- ✅ Environment variable configuration
- ✅ In-memory storage (easily upgradeable to database)

## Tech Stack

**Frontend:**
- React 19
- Vite
- Firebase SDK 10.14.1
- Axios
- Service Workers

**Backend:**
- Java 17
- Spring Boot 3.2.0
- Firebase Admin SDK
- Maven

## Quick Start

### Prerequisites

- Node.js 18+ and npm
- Java 17+
- Maven
- Firebase project with Cloud Messaging enabled

### 1. Clone and Setup

```bash
git clone <your-repo-url>
cd proof-of-concept-website-with-notification
```

### 2. Configure Environment Variables

**Frontend:**
```bash
cd notification-frontend
cp .env.example .env
# Edit .env with your Firebase credentials
```

**Backend:**
```bash
cd notification-backend
cp .env.example .env
# Edit .env with your Firebase service account path
```

See [ENV_SETUP_GUIDE.md](./ENV_SETUP_GUIDE.md) for detailed instructions.

### 3. Install Dependencies

**Frontend:**
```bash
cd notification-frontend
npm install
```

**Backend:**
```bash
cd notification-backend
mvn clean install
```

### 4. Run the Application

**Terminal 1 - Backend:**
```bash
cd notification-backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd notification-frontend
npm run dev
```

Open http://localhost:5173 in your browser.

### 5. Enable Push Notifications

1. Click the "Enable" button on the push notification toggle
2. Grant browser permission when prompted
3. Create a notification using the form
4. You'll receive a push notification! 🎉

## Project Structure

```
.
├── notification-frontend/          # React frontend
│   ├── src/
│   │   ├── components/            # React components
│   │   ├── services/              # API and push notification services
│   │   └── config/                # Firebase configuration
│   ├── public/                    # Static assets
│   └── .env.example               # Environment variables template
│
├── notification-backend/           # Spring Boot backend
│   ├── src/main/java/com/notification/
│   │   ├── controller/            # REST controllers
│   │   ├── service/               # Business logic
│   │   ├── repository/            # Data access
│   │   ├── model/                 # Domain models
│   │   └── config/                # Configuration
│   └── .env.example               # Environment variables template
│
├── ENV_SETUP_GUIDE.md             # Environment setup instructions
├── FIREBASE_SETUP_GUIDE.md        # Firebase configuration guide
└── PUSH_NOTIFICATIONS_README.md   # Push notifications documentation
```

## API Endpoints

### Notifications

- `POST /api/notifications` - Create a notification
- `GET /api/notifications` - Get all notifications

### Push Subscriptions

- `POST /api/notifications/subscribe` - Subscribe to push notifications
- `POST /api/notifications/unsubscribe` - Unsubscribe from push notifications

## Documentation

- **[ENV_SETUP_GUIDE.md](./ENV_SETUP_GUIDE.md)** - Environment variables setup
- **[FIREBASE_SETUP_GUIDE.md](./FIREBASE_SETUP_GUIDE.md)** - Firebase configuration
- **[PUSH_NOTIFICATIONS_README.md](./PUSH_NOTIFICATIONS_README.md)** - Push notifications overview
- **[SETUP_CHECKLIST.md](./SETUP_CHECKLIST.md)** - Quick setup checklist

## Development

### Frontend Development

```bash
cd notification-frontend
npm run dev          # Start dev server
npm run build        # Build for production
npm run test         # Run tests
npm run lint         # Lint code
```

### Backend Development

```bash
cd notification-backend
mvn spring-boot:run  # Start server
mvn test             # Run tests
mvn clean package    # Build JAR
```

## Testing Push Notifications

### Test Foreground Notifications
1. Keep the app open
2. Create a notification
3. Should see notification popup

### Test Background Notifications
1. Enable push notifications
2. Minimize or close the browser tab
3. Create a notification from another device/browser
4. Should receive OS notification

## Browser Support

- ✅ Chrome/Edge (Desktop & Android)
- ✅ Firefox (Desktop & Android)
- ✅ Safari 16.4+ (macOS & iOS)
- ✅ Opera
- ❌ Internet Explorer

## Security

- Environment variables for sensitive credentials
- Firebase service account key not committed to git
- CORS configuration for API security
- HTTPS required in production for push notifications

## Deployment

### Frontend (Vercel/Netlify)

1. Connect your git repository
2. Add environment variables in dashboard
3. Deploy!

### Backend (Heroku/AWS)

1. Set environment variables
2. Upload Firebase service account key securely
3. Deploy JAR file

See [ENV_SETUP_GUIDE.md](./ENV_SETUP_GUIDE.md) for detailed deployment instructions.

## Troubleshooting

### Push notifications not working?
- Check [TROUBLESHOOTING_STUCK_TOKEN.md](./TROUBLESHOOTING_STUCK_TOKEN.md)
- Verify Firebase Cloud Messaging is enabled
- Check browser console for errors
- Try in incognito mode

### Backend won't start?
- Check `.env` file exists and has correct values
- Verify Firebase service account path is correct
- Check Java version (requires 17+)

### Frontend build fails?
- Check `.env` file exists
- Run `npm run generate-sw` manually
- Clear node_modules and reinstall

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

MIT License - feel free to use this project for learning or production!

## Acknowledgments

- Firebase for Cloud Messaging
- Spring Boot team
- React team
- Vite team

---

**Need help?** Check the documentation files or open an issue!
