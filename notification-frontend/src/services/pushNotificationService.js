import { messaging, getToken, onMessage } from '../config/firebase';
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

// VAPID key from environment variables
const VAPID_KEY = import.meta.env.VITE_FIREBASE_VAPID_KEY;

/**
 * Request notification permission from the user
 * @returns {Promise<string>} Permission status: 'granted', 'denied', or 'default'
 */
export const requestNotificationPermission = async () => {
    try {
        const permission = await Notification.requestPermission();
        console.log('Notification permission:', permission);
        return permission;
    } catch (error) {
        console.error('Error requesting notification permission:', error);
        throw error;
    }
};

/**
 * Get FCM token for this device/browser
 * @returns {Promise<string>} FCM token
 */
export const getFCMToken = async () => {
    try {
        console.log('🔍 Starting FCM token retrieval...');

        // Check if service worker is supported
        if (!('serviceWorker' in navigator)) {
            throw new Error('Service workers are not supported in this browser');
        }
        console.log('✅ Service workers supported');

        // Register service worker
        console.log('📝 Registering service worker...');
        const registration = await navigator.serviceWorker.register(
            '/firebase-messaging-sw.js'
        );
        console.log('✅ Service Worker registered:', registration);

        // If there's a waiting service worker, activate it immediately
        if (registration.waiting) {
            console.log('⏳ Waiting service worker detected, activating...');
            registration.waiting.postMessage({ type: 'SKIP_WAITING' });
            // Wait for the new service worker to activate
            await new Promise((resolve) => {
                navigator.serviceWorker.addEventListener('controllerchange', resolve, { once: true });
            });
            console.log('✅ Service worker activated');
        }

        // Wait for service worker to be ready
        console.log('⏳ Waiting for service worker to be ready...');
        await navigator.serviceWorker.ready;
        console.log('✅ Service worker ready');

        // Get FCM token
        console.log('🔑 Requesting FCM token with VAPID key...');
        console.log('VAPID Key length:', VAPID_KEY.length);
        console.log('VAPID Key (first 20 chars):', VAPID_KEY.substring(0, 20) + '...');

        // Add timeout to detect hanging
        const tokenPromise = getToken(messaging, {
            vapidKey: VAPID_KEY,
            serviceWorkerRegistration: registration
        });

        const timeoutPromise = new Promise((_, reject) => {
            setTimeout(() => reject(new Error('⏱️ FCM token request timed out after 30 seconds. Possible issues: 1) Check service worker console for errors, 2) Verify Firebase project has Cloud Messaging enabled, 3) Check Network tab for failed requests to Firebase')), 30000);
        });

        console.log('⏳ Waiting for FCM token (max 30s)...');
        const token = await Promise.race([tokenPromise, timeoutPromise]);

        console.log('token:', token);
        if (token) {
            console.log('✅ FCM Token received:', token.substring(0, 20) + '...');
            return token;
        } else {
            throw new Error('No registration token available. Check Firebase project settings.');
        }
    } catch (error) {
        console.error('❌ Error getting FCM token:', error);
        console.error('Error details:', {
            name: error.name,
            message: error.message,
            code: error.code,
            stack: error.stack
        });
        throw error;
    }
};

/**
 * Subscribe to push notifications
 * Requests permission, gets FCM token, and registers it with the backend
 * @returns {Promise<Object>} Subscription result
 */
export const subscribeToPushNotifications = async () => {
    try {
        // Request permission
        const permission = await requestNotificationPermission();

        if (permission !== 'granted') {
            throw new Error('Notification permission denied');
        }

        // Get FCM token
        const token = await getFCMToken();

        // Register token with backend
        const response = await axios.post(`${API_BASE_URL}/api/notifications/subscribe`, {
            token
        });

        console.log('Subscribed to push notifications:', response.data);

        // Store token in localStorage for reference
        localStorage.setItem('fcm_token', token);
        localStorage.setItem('push_subscribed', 'true');

        return {
            success: true,
            token,
            message: 'Successfully subscribed to push notifications'
        };
    } catch (error) {
        console.error('Error subscribing to push notifications:', error);
        throw error;
    }
};

/**
 * Unsubscribe from push notifications
 * @returns {Promise<Object>} Unsubscribe result
 */
export const unsubscribeFromPushNotifications = async () => {
    try {
        const token = localStorage.getItem('fcm_token');

        if (!token) {
            throw new Error('No FCM token found');
        }

        // Unregister token with backend
        await axios.post(`${API_BASE_URL}/api/notifications/unsubscribe`, {
            token
        });

        // Clear local storage
        localStorage.removeItem('fcm_token');
        localStorage.removeItem('push_subscribed');

        return {
            success: true,
            message: 'Successfully unsubscribed from push notifications'
        };
    } catch (error) {
        console.error('Error unsubscribing from push notifications:', error);
        throw error;
    }
};

/**
 * Check if user is subscribed to push notifications
 * @returns {boolean} Subscription status
 */
export const isPushSubscribed = () => {
    return localStorage.getItem('push_subscribed') === 'true';
};

/**
 * Setup foreground message listener
 * Handles notifications when the app is open
 * @param {Function} callback - Function to call when message is received
 */
export const setupForegroundMessageListener = (callback) => {
    onMessage(messaging, (payload) => {
        console.log('Foreground message received:', payload);

        // Show browser notification even when app is in foreground
        if (Notification.permission === 'granted') {
            const notificationTitle = payload.notification?.title || 'New Notification';
            const notificationOptions = {
                body: payload.notification?.body || 'You have a new notification',
                icon: '/notification-icon.png',
                tag: payload.data?.notificationId || 'notification'
            };

            new Notification(notificationTitle, notificationOptions);
        }

        // Call the callback with the payload
        if (callback) {
            callback(payload);
        }
    });
};

export default {
    requestNotificationPermission,
    getFCMToken,
    subscribeToPushNotifications,
    unsubscribeFromPushNotifications,
    isPushSubscribed,
    setupForegroundMessageListener
};
