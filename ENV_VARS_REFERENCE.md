# Environment Variables Reference (AI Generated)

## Local Development

When running the backend and frontend separately:

**Backend** (`snowsense/notification-backend/.env`):
```bash
FIREBASE_SERVICE_ACCOUNT_PATH=/absolute/path/to/firebase-service-account.json
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

**Frontend** (`snowsense/app/.env`):
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

## Production (Render/Hugging Face)

Set these in your deployment platform's environment variables:

```bash
# Firebase - Use base64 encoded credentials
FIREBASE_SERVICE_ACCOUNT_BASE64=<your-base64-encoded-json>

# Server
SERVER_PORT=8080

# CORS - Use your production frontend URL
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

## Why Two Methods for Firebase Credentials?

The application supports two ways of providing the Firebase admin credentials to the backend:

**File Path** (`FIREBASE_SERVICE_ACCOUNT_PATH`):
- ✅ Easy for local development.
- ✅ You can see the JSON file.
- ❌ Harder to use in cloud environments where you can't easily upload files.

**Base64** (`FIREBASE_SERVICE_ACCOUNT_BASE64`):
- ✅ Perfect for cloud deployment (Render, Hugging Face, etc.).
- ✅ You just need to copy and paste a single string.
- ❌ The string is not human-readable.

The application will first try to use the `FIREBASE_SERVICE_ACCOUNT_BASE64` variable. If it's not present, it will fall back to using the `FIREBASE_SERVICE_ACCOUNT_PATH`.

## Troubleshooting

**"Firebase not initialized" warning:**
- Make sure the environment variable you are using is set correctly.
- For the file path method, verify that the path is correct and the file exists.
- For the base64 method, ensure you have copied the entire string.

**CORS errors in browser:**
- Make sure `CORS_ALLOWED_ORIGINS` on the backend matches the URL of the frontend.
- For local development, this is usually `http://localhost:5173`.
- For production, it will be your actual frontend URL.

**Port already in use:**
- Change `SERVER_PORT` in your backend's `.env` file to a different port (e.g., 8081).
- Alternatively, find and stop the other process that is using port 8080.
