# Environment Variables Reference

## Local Development (Separate Services)

When running `mvn spring-boot:run` and `npm run dev` separately:

**Backend** (`notification-backend/.env`):
```bash
FIREBASE_SERVICE_ACCOUNT_PATH=/absolute/path/to/firebase-service-account.json
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

**Frontend** (`notification-frontend/.env`):
```bash
VITE_FIREBASE_API_KEY=your_api_key
VITE_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=your_project_id
VITE_FIREBASE_STORAGE_BUCKET=your_project.appspot.com
VITE_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
VITE_FIREBASE_APP_ID=your_app_id
VITE_FIREBASE_VAPID_KEY=your_vapid_key
VITE_API_URL=http://localhost:8080
```

## Local Testing (Packaged JAR)

When running `./run-packaged-app.sh`:

Uses the same `notification-backend/.env` but automatically updates CORS:
```bash
FIREBASE_SERVICE_ACCOUNT_PATH=/absolute/path/to/firebase-service-account.json
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:8080  # Updated automatically
```

## Production (Render/Railway)

Set these in your deployment platform's environment variables:

```bash
# Firebase - Use base64 encoded credentials
FIREBASE_SERVICE_ACCOUNT_BASE64=<your-base64-encoded-json>

# Server
SERVER_PORT=8080

# CORS - Use your production URL
CORS_ALLOWED_ORIGINS=https://your-app.onrender.com
```

### Generate Base64 Firebase Credentials

**Linux/WSL:**
```bash
base64 -w 0 firebase-service-account.json
```

**macOS:**
```bash
base64 -i firebase-service-account.json
```

**Windows (PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("firebase-service-account.json"))
```

Copy the entire output (one long string) and use it as `FIREBASE_SERVICE_ACCOUNT_BASE64`.

## Why Two Methods?

**File Path** (`FIREBASE_SERVICE_ACCOUNT_PATH`):
- ✅ Easy for local development
- ✅ Human-readable JSON file
- ❌ Harder to deploy (need to upload file)

**Base64** (`FIREBASE_SERVICE_ACCOUNT_BASE64`):
- ✅ Perfect for cloud deployment
- ✅ Just paste as environment variable
- ✅ No file upload needed
- ❌ Not human-readable

The app checks for base64 first, then falls back to file path. Use whichever is easier for your environment!

## Troubleshooting

**"Firebase not initialized" warning?**
- Check that environment variable is set correctly
- For file path: verify the file exists at that location
- For base64: ensure the entire string was copied (no truncation)

**CORS errors in browser?**
- Make sure `CORS_ALLOWED_ORIGINS` matches the URL you're accessing
- Local dev: `http://localhost:5173` (separate services) or `http://localhost:8080` (packaged)
- Production: Your actual Render/Railway URL

**Port already in use?**
- Change `SERVER_PORT` to a different port (e.g., 8081)
- Or stop other services using port 8080
