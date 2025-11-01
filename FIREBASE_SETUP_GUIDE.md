# Firebase Push Notifications Setup Guide

## Prerequisites

- A Google account
- Node.js and npm installed
- Java 17+ and Maven installed

## Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select an existing project
3. Follow the setup wizard (you can disable Google Analytics if not needed)

## Step 2: Register Your Web App

1. In your Firebase project, click the **Web icon** (</>) to add a web app
2. Give your app a nickname (e.g., "Notification System")
3. Check "Also set up Firebase Hosting" (optional)
4. Click "Register app"
5. Copy the Firebase configuration object - you'll need this!

It looks like this:
```javascript
const firebaseConfig = {
  apiKey: "AIzaSyXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  authDomain: "your-project.firebaseapp.com",
  projectId: "your-project-id",
  storageBucket: "your-project.appspot.com",
  messagingSenderId: "123456789012",
  appId: "1:123456789012:web:abcdef123456"
};
```

## Step 3: Enable Cloud Messaging

1. In Firebase Console, go to **Project Settings** (gear icon)
2. Navigate to the **Cloud Messaging** tab
3. Under "Web configuration", find the **Web Push certificates** section
4. Click "Generate key pair" to create a VAPID key
5. Copy the key pair value - you'll need this!

## Step 4: Configure Frontend

### 4.1 Update Firebase Config

Edit `notification-frontend/src/config/firebase.js`:

```javascript
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",           // Replace with your values
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID"
};
```

### 4.2 Update Service Worker

Edit `notification-frontend/public/firebase-messaging-sw.js`:

Replace the Firebase config in the service worker with the same values:

```javascript
firebase.initializeApp({
  apiKey: "YOUR_API_KEY",           // Same values as above
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID"
});
```

### 4.3 Update VAPID Key

Edit `notification-frontend/src/services/pushNotificationService.js`:

```javascript
const VAPID_KEY = 'YOUR_VAPID_KEY_FROM_STEP_3';
```

## Step 5: Configure Backend

### 5.1 Download Service Account Key

1. In Firebase Console, go to **Project Settings** → **Service Accounts**
2. Click "Generate new private key"
3. Save the JSON file securely (e.g., `firebase-service-account.json`)
4. **IMPORTANT**: Never commit this file to version control!

### 5.2 Configure Application Properties

Create or edit `notification-backend/src/main/resources/application.properties`:

```properties
# Firebase Configuration
firebase.service-account-key-path=/path/to/your/firebase-service-account.json

# Server Configuration
server.port=8080

# CORS Configuration (if needed)
spring.web.cors.allowed-origins=http://localhost:5173
```

**For production**, use environment variables:
```properties
firebase.service-account-key-path=${FIREBASE_SERVICE_ACCOUNT_PATH}
```

### 5.3 Add Service Account to .gitignore

Add to `notification-backend/.gitignore`:
```
firebase-service-account.json
*.json
```

## Step 6: Test the Setup

### 6.1 Start Backend

```bash
cd notification-backend
mvn spring-boot:run
```

You should see: `Firebase initialized successfully`

### 6.2 Start Frontend

```bash
cd notification-frontend
npm run dev
```

### 6.3 Test Push Notifications

1. Open the app in your browser (http://localhost:5173)
2. You'll see a "Push Notifications" toggle at the top
3. Click "Enable" - browser will ask for permission
4. Grant notification permission
5. Create a new notification using the form
6. You should receive a push notification!

### 6.4 Test Background Notifications

1. Minimize or switch to another tab
2. Create a notification from another browser/device
3. You should receive a notification even though the tab isn't active!

## Browser Support

Push notifications work on:
- ✅ Chrome/Edge (Desktop & Android)
- ✅ Firefox (Desktop & Android)
- ✅ Safari 16.4+ (macOS & iOS)
- ✅ Opera
- ❌ Internet Explorer

## Additional Resources

- [Firebase Cloud Messaging Documentation](https://firebase.google.com/docs/cloud-messaging)
- [Web Push Notifications Guide](https://web.dev/push-notifications-overview/)
- [Service Worker API](https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API)
