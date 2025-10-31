import axios from 'axios';

// Configure base URL - use environment variable or default to localhost
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

// Create Axios instance with base configuration
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 second timeout
});

// Add response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle different error scenarios
    if (error.response) {
      // Server responded with error status
      const errorMessage = error.response.data?.error || 'An error occurred';
      const errorDetails = error.response.data?.details || [];
      
      return Promise.reject({
        message: errorMessage,
        details: errorDetails,
        status: error.response.status,
      });
    } else if (error.request) {
      // Request made but no response received
      return Promise.reject({
        message: 'Unable to connect to the server. Please check your connection.',
        details: [],
        status: null,
      });
    } else {
      // Error in request setup
      return Promise.reject({
        message: 'An unexpected error occurred',
        details: [],
        status: null,
      });
    }
  }
);

/**
 * Create a new notification
 * @param {Object} notificationData - The notification data
 * @param {string} notificationData.title - The notification title
 * @param {string} notificationData.message - The notification message
 * @returns {Promise<Object>} The created notification with id and timestamp
 */
export const createNotification = async (notificationData) => {
  const response = await apiClient.post('/api/notifications', notificationData);
  return response.data;
};

/**
 * Get all notifications
 * @returns {Promise<Array>} Array of all notifications
 */
export const getAllNotifications = async () => {
  const response = await apiClient.get('/api/notifications');
  return response.data;
};

export default {
  createNotification,
  getAllNotifications,
};
