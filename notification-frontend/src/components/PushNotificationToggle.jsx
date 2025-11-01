import { useState, useEffect } from 'react';
import {
  subscribeToPushNotifications,
  unsubscribeFromPushNotifications,
  isPushSubscribed
} from '../services/pushNotificationService';
import './PushNotificationToggle.css';

function PushNotificationToggle() {
  const [isSubscribed, setIsSubscribed] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState(''); // 'success' or 'error'

  useEffect(() => {
    // Check subscription status on mount
    setIsSubscribed(isPushSubscribed());
  }, []);

  const showMessage = (text, type) => {
    setMessage(text);
    setMessageType(type);
    setTimeout(() => {
      setMessage('');
      setMessageType('');
    }, 5000);
  };

  const handleToggle = async () => {
    setIsLoading(true);
    setMessage('');

    try {
      if (isSubscribed) {
        // Unsubscribe
        await unsubscribeFromPushNotifications();
        setIsSubscribed(false);
        showMessage('Push notifications disabled', 'success');
      } else {
        // Subscribe
        const result = await subscribeToPushNotifications();
        setIsSubscribed(true);
        showMessage('Push notifications enabled! You\'ll receive notifications even when this tab is closed.', 'success');
      }
    } catch (error) {
      console.error('Error toggling push notifications:', error);
      showMessage(
        error.message || 'Failed to update push notification settings',
        'error'
      );
    } finally {
      setIsLoading(false);
    }
  };

  // Check if notifications are supported
  if (!('Notification' in window)) {
    return (
      <div className="push-notification-toggle">
        <div className="alert alert-warning">
          Push notifications are not supported in this browser.
        </div>
      </div>
    );
  }

  return (
    <div className="push-notification-toggle">
      <div className="toggle-container">
        <div className="toggle-info">
          <h3>🔔 Push Notifications</h3>
          <p>
            {isSubscribed
              ? 'You\'re receiving push notifications'
              : 'Get notified even when this page is closed'}
          </p>
        </div>
        
        <button
          onClick={handleToggle}
          disabled={isLoading}
          className={`toggle-button ${isSubscribed ? 'subscribed' : ''} ${isLoading ? 'loading' : ''}`}
        >
          {isLoading ? (
            'Processing...'
          ) : isSubscribed ? (
            'Disable'
          ) : (
            'Enable'
          )}
        </button>
      </div>

      {message && (
        <div className={`alert alert-${messageType}`}>
          {message}
        </div>
      )}

      {isSubscribed && (
        <div className="subscription-info">
          <small>
            ℹ️ You'll receive notifications for all new messages created on this site.
          </small>
        </div>
      )}
    </div>
  );
}

export default PushNotificationToggle;
