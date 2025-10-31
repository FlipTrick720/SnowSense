import './NotificationItem.css';

function NotificationItem({ notification }) {
  const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    
    // Format: "Jan 15, 2025 at 3:45 PM"
    const options = {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    };
    
    return date.toLocaleString('en-US', options);
  };

  return (
    <div className="notification-item">
      <div className="notification-header">
        <h3 className="notification-title">{notification.title}</h3>
        <span className="notification-timestamp">
          {formatTimestamp(notification.timestamp)}
        </span>
      </div>
      <p className="notification-message">{notification.message}</p>
    </div>
  );
}

export default NotificationItem;
