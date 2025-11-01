import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { config } from 'dotenv';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load environment variables from .env file
config();

// Read the template
const template = fs.readFileSync(
  path.join(__dirname, 'public', 'firebase-messaging-sw.template.js'),
  'utf-8'
);

// Replace placeholders with environment variables
const serviceWorker = template
  .replace('__VITE_FIREBASE_API_KEY__', process.env.VITE_FIREBASE_API_KEY || '')
  .replace('__VITE_FIREBASE_AUTH_DOMAIN__', process.env.VITE_FIREBASE_AUTH_DOMAIN || '')
  .replace('__VITE_FIREBASE_PROJECT_ID__', process.env.VITE_FIREBASE_PROJECT_ID || '')
  .replace('__VITE_FIREBASE_STORAGE_BUCKET__', process.env.VITE_FIREBASE_STORAGE_BUCKET || '')
  .replace('__VITE_FIREBASE_MESSAGING_SENDER_ID__', process.env.VITE_FIREBASE_MESSAGING_SENDER_ID || '')
  .replace('__VITE_FIREBASE_APP_ID__', process.env.VITE_FIREBASE_APP_ID || '');

// Write the generated service worker
fs.writeFileSync(
  path.join(__dirname, 'public', 'firebase-messaging-sw.js'),
  serviceWorker
);

console.log('✅ Service worker generated with environment variables');
