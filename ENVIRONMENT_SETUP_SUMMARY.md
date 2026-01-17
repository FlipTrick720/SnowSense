# Environment Variables Setup - Summary (AI Generated)

## ✅ What Was Done

This project uses professional environment variable management.

### Files Created

**Frontend (`snowsense/app`):**
- ✅ `.env` - Your actual credentials (NOT in git)
- ✅ `.env.example` - Template for team members (IN git)

**Backend (`snowsense/notification-backend`):**
- ✅ `.env` - Your actual credentials (NOT in git)
- ✅ `.env.example` - Template for team members (IN git)

**Documentation:**
- ✅ `ENV_SETUP_GUIDE.md` - Complete setup guide
- ✅ `ENV_VARS_REFERENCE.md` - Quick reference for all variables
- ✅ `README.md` - Project overview
- ✅ `ENVIRONMENT_SETUP_SUMMARY.md` - This file

### Files Modified

**Frontend:**
- ✅ `src/main.tsx` - Now uses `import.meta.env.VITE_*` for Firebase config.

**Backend:**
- ✅ `src/main/resources/application.properties` - Now uses `${ENV_VAR}`.
- ✅ `pom.xml` - Added `spring-dotenv` dependency to load `.env` files.

**Root:**
- ✅ `.gitignore` - Excludes secrets like `.env` files and `firebase-service-account.json`.

## 🔒 What's Protected

These files are now **excluded from git** and should never be committed:
- All `.env` files.
- `firebase-service-account.json`.

These files are **included in git** (safe to commit):
- All `.env.example` files (they contain templates, not real secrets).
- All source code that reads environment variables.

## 🚀 How It Works

### Frontend

**Development (`npm run dev`):**
1. Vite loads the `.env` file from `snowsense/app`.
2. Vite injects the `import.meta.env.VITE_*` variables into the application at build time.

**Production (`npm run build`):**
1. Your deployment platform (e.g., Render) sets the environment variables.
2. Vite uses these variables to create an optimized production build.

### Backend

**Development (`mvn spring-boot:run`):**
1. The `spring-dotenv` library loads the `.env` file from `snowsense/notification-backend`.
2. Spring Boot replaces placeholders like `${SERVER_PORT}` in `application.properties` with the actual values.

**Production:**
1. Your deployment platform sets the environment variables.
2. Spring Boot reads the variables directly from the system environment.

## 📋 Quick Reference

### Frontend Environment Variables (`snowsense/app/.env`)

```bash
VITE_FIREBASE_API_KEY=your_key
VITE_FIREBASE_AUTH_DOMAIN=your_domain
VITE_FIREBASE_PROJECT_ID=your_project
VITE_FIREBASE_STORAGE_BUCKET=your_bucket
VITE_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
VITE_FIREBASE_APP_ID=your_app_id
VITE_FIREBASE_VAPID_KEY=your_vapid_key
VITE_API_URL=http://localhost:8080
```

### Backend Environment Variables (`snowsense/notification-backend/.env`)

```bash
FIREBASE_SERVICE_ACCOUNT_PATH=/path/to/key.json
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

## 🎓 Best Practices Implemented

1. ✅ **Separation of config from code** (a core principle of the 12-factor app).
2. ✅ **No secrets in version control**.
3. ✅ **Environment-specific configuration** (development vs. production).
4. ✅ **Template files for easy setup** (`.env.example`).
5. ✅ **Clear documentation** for setup and reference.
6. ✅ **Security by default** (secrets are ignored in `.gitignore`).

