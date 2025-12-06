# Environment Variables Setup Guide (AI Generated)

This project uses environment variables to keep sensitive credentials secure and out of version control.

## Quick Start

### 1. Frontend Setup

```bash
cd notification-frontend

# Copy the example file
cp .env.example .env

# Edit .env with your Firebase credentials
nano .env  # or use your favorite editor
```

**Required variables in `.env`:**
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

### 2. Backend Setup

```bash
cd notification-backend

# Copy the example file
cp .env.example .env

# Edit .env with your paths
nano .env
```

**Required variables in `.env`:**
```bash
FIREBASE_SERVICE_ACCOUNT_PATH=/absolute/path/to/firebase-service-account.json
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

### 3. Get Your Firebase Credentials

#### Firebase Config (Frontend)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Click gear icon → **Project Settings**
4. Scroll to **Your apps** section
5. Click on your web app (or create one)
6. Copy the `firebaseConfig` object values

#### VAPID Key (Frontend)
1. In Firebase Console → **Project Settings**
2. Go to **Cloud Messaging** tab
3. Scroll to **Web Push certificates**
4. Click **Generate key pair** (if not already generated)
5. Copy the key pair value

#### Service Account Key (Backend)
1. In Firebase Console → **Project Settings**
2. Go to **Service Accounts** tab
3. Click **Generate new private key**
4. Save the JSON file securely
5. Put the absolute path in your backend `.env` file

## Running the Application

### Frontend
```bash
cd notification-frontend

# Install dependencies (first time only)
npm install

# Generate service worker and start dev server
npm run dev
```

The `npm run dev` command will:
1. Generate `firebase-messaging-sw.js` from template with your env vars
2. Start the Vite dev server

### Backend
```bash
cd notification-backend

# The .env file is automatically loaded by spring-dotenv
mvn spring-boot:run
```

## File Structure

```
project-root/
├── .gitignore                                    # Excludes .env files
├── firebase-service-account.json                 # ❌ NOT in git
│
├── notification-frontend/
│   ├── .env                                      # ❌ NOT in git (your secrets)
│   ├── .env.example                              # ✅ IN git (template)
│   ├── generate-sw.js                            # ✅ IN git (generator script)
│   ├── public/
│   │   ├── firebase-messaging-sw.js              # ❌ NOT in git (generated)
│   │   └── firebase-messaging-sw.template.js     # ✅ IN git (template)
│   └── src/
│       ├── config/firebase.js                    # ✅ Uses env vars
│       └── services/pushNotificationService.js   # ✅ Uses env vars
│
└── notification-backend/
    ├── .env                                      # ❌ NOT in git (your secrets)
    ├── .env.example                              # ✅ IN git (template)
    └── src/main/resources/
        └── application.properties                # ✅ Uses env vars
```

## What Gets Committed to Git?

### ✅ Committed (Safe)
- `.env.example` files (templates with placeholder values)
- `firebase-messaging-sw.template.js` (template with placeholders)
- `generate-sw.js` (script to generate service worker)
- Code files that reference `import.meta.env.VARIABLE_NAME`
- `application.properties` with `${ENV_VAR}` syntax

### ❌ NOT Committed (Secrets)
- `.env` files (contain real credentials)
- `firebase-service-account.json` (Firebase admin credentials)
- `firebase-messaging-sw.js` (generated file with real credentials)

## For Team Members

When a new team member clones the repo:

1. **Copy example files:**
   ```bash
   cp notification-frontend/.env.example notification-frontend/.env
   cp notification-backend/.env.example notification-backend/.env
   ```

2. **Get credentials from own Firebase account**

3. **Fill in the `.env` files** with real values

4. **Download Firebase service account key** and save it securely

5. **Run the app:**
   ```bash
   # Frontend
   cd notification-frontend && npm install && npm run dev
   
   # Backend (in another terminal)
   cd notification-backend && mvn spring-boot:run
   ```

## Production Deployment

### Frontend (Vercel, Netlify, etc.)

Add environment variables in your hosting platform's dashboard:

**Vercel:**
1. Project Settings → Environment Variables
2. Add each `VITE_*` variable
3. Redeploy

**Netlify:**
1. Site Settings → Build & Deploy → Environment
2. Add each `VITE_*` variable
3. Trigger new deploy
