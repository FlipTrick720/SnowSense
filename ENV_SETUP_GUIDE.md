# Environment Variables Setup Guide (AI Generated)

This project uses environment variables to keep sensitive credentials secure and out of version control.

## Quick Start

### 1. Backend Setup

```bash
cd snowsense/notification-backend

# Copy the example file
cp .env.example .env

# Edit .env with your paths
nano .env # or use your favorite editor
```

**Required variables in `snowsense/notification-backend/.env`:**
```bash
FIREBASE_SERVICE_ACCOUNT_PATH=/absolute/path/to/firebase-service-account.json
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

### 2. Frontend Setup

```bash
cd snowsense/app

# Copy the example file
cp .env.example .env

# Edit .env with your Firebase credentials
nano .env  # or use your favorite editor
```

**Required variables in `snowsense/app/.env`:**
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

### 3. Get Your Firebase Credentials

You need a Firebase project to run the application.

#### Firebase Config (Frontend)
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Select your project.
3. Go to **Project Settings** (gear icon) > **General**.
4. In the **Your apps** section, select your web app (or create a new one).
5. Copy the `firebaseConfig` object values and put them in `snowsense/app/.env`.

#### VAPID Key (Frontend)
1. In the Firebase Console, go to **Project Settings** > **Cloud Messaging**.
2. Under **Web configuration**, find the **Web Push certificates** section.
3. Click **Generate key pair** if you don't have one.
4. Copy the key pair value and put it in `snowsense/app/.env` as `VITE_FIREBASE_VAPID_KEY`.

#### Service Account Key (Backend)
1. In the Firebase Console, go to **Project Settings** > **Service Accounts**.
2. Click **Generate new private key**.
3. Save the downloaded JSON file in a secure location (e.g., outside the project directory).
4. Add the absolute path to this file in `snowsense/notification-backend/.env`.

## Running the Application

### Backend
```bash
cd snowsense/notification-backend

# The .env file is automatically loaded by spring-dotenv
mvn spring-boot:run
```

### Frontend
```bash
cd snowsense/app

# Install dependencies (first time only)
npm install

# Start the Vite dev server
npm run dev
```

## File Structure

```
tmpSnowSense/
└── snowsense/
    ├── .gitignore
    ├── notification-backend/
    │   ├── .env                # ❌ NOT in git (your secrets)
    │   ├── .env.example        # ✅ IN git (template)
    │   └── src/main/resources/
    │       └── application.properties # ✅ Uses env vars
    │
    └── app/
        ├── .env                # ❌ NOT in git (your secrets)
        ├── .env.example        # ✅ IN git (template)
        └── src/
            └── main.tsx        # ✅ Uses env vars for Firebase config
```

## What Gets Committed to Git?

### ✅ Committed (Safe)
- `.env.example` files (templates with placeholder values).
- Source code that reads environment variables (e.g., `import.meta.env.VITE_*` in the frontend, and `${...}` in `application.properties` in the backend).

### ❌ NOT Committed (Secrets)
- `.env` files (these contain your actual credentials).
- The `firebase-service-account.json` file.

## For New Team Members

When a new team member clones the repo:

1.  **Copy example files:**
    ```bash
    cp snowsense/notification-backend/.env.example snowsense/notification-backend/.env
    cp snowsense/app/.env.example snowsense/app/.env
    ```
2.  **Get credentials** from their own Firebase account.
3.  **Fill in the `.env` files** with the real values.
4.  **Download the Firebase service account key** and set the path in the backend's `.env` file.
5.  **Run the app:**
    ```bash
    # Backend (in one terminal)
    cd snowsense/notification-backend
    mvn spring-boot:run
    
    # Frontend (in another terminal)
    cd snowsense/app
    npm install
    npm run dev
    ```

## Production Deployment

When deploying to a platform like Render or Hugging Face, you don't use `.env` files. Instead, you set the environment variables directly in the platform's dashboard. Refer to `ENV_VARS_REFERENCE.md` for a list of variables.
