// Firebase Cloud Messaging Service Worker
// This runs in the background to handle push notifications

// Use Firebase v10.14.1 to match the main app
importScripts('https://www.gstatic.com/firebasejs/10.14.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.14.1/firebase-messaging-compat.js');

// Initialize Firebase in the service worker
// These values are replaced at build time from environment variables
firebase.initializeApp({
  apiKey: "AIzaSyDtOpG_E1ZyNgpuxlnzSQHzA6H_ViSw8fs",
  authDomain: "poc-pushnotification-fee02.firebaseapp.com",
  projectId: "poc-pushnotification-fee02",
  storageBucket: "poc-pushnotification-fee02.firebasestorage.app",
  messagingSenderId: "734189470445",
  appId: "1:734189470445:web:a4c8c9322c44ba983d02e4"
});

const messaging = firebase.messaging();

// Handle skip waiting message (for immediate activation)
self.addEventListener('message', (event) => {
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting();
  }
});

// Handle background messages
messaging.onBackgroundMessage((payload) => {
  console.log('Received background message:', payload);

  const notificationTitle = payload.notification?.title || 'New Notification';
  const notificationOptions = {
    body: payload.notification?.body || 'You have a new notification',
    icon: '/notification-icon.png',
    badge: '/notification-badge.png',
    tag: payload.data?.notificationId || 'notification',
    data: payload.data,
    requireInteraction: false,
    vibrate: [200, 100, 200]
  };

  return self.registration.showNotification(notificationTitle, notificationOptions);
});

// Handle notification click
self.addEventListener('notificationclick', (event) => {
  console.log('Notification clicked:', event);
  
  event.notification.close();

  // Open or focus the app window
  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true })
      .then((clientList) => {
        // If a window is already open, focus it
        for (const client of clientList) {
          if (client.url.includes(self.registration.scope) && 'focus' in client) {
            return client.focus();
          }
        }
        // Otherwise, open a new window
        if (clients.openWindow) {
          return clients.openWindow('/');
        }
      })
  );
});
