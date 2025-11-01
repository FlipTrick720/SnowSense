import NotificationItem from './NotificationItem';
import './NotificationList.css';

function NotificationList({ notifications, isLoading }) {
  if (isLoading) {
    return (
      <div className="notification-list-container">
        <h2>Notification History</h2>
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <div className="loading-text">Loading notifications...</div>
        </div>
      </div>
    );
  }

  if (!notifications || notifications.length === 0) {
    return (
      <div className="notification-list-container">
        <h2>Notification History</h2>
        <div className="empty-message">
          No notifications yet. Create your first notification above!
        </div>
      </div>
    );
  }

  return (
    <div className="notification-list-container">
      <h2>Notification History</h2>
      <div className="notification-count">
        {notifications.length} {notifications.length === 1 ? 'notification' : 'notifications'}
      </div>
      <div className="notification-list">
        {notifications.map((notification) => (
          <NotificationItem 
            key={notification.id} 
            notification={notification} 
          />
        ))}
      </div>
    </div>
  );
}

export default NotificationList;
