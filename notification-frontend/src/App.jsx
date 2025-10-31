import { useState, useEffect } from 'react';
import NotificationForm from './components/NotificationForm';
import NotificationList from './components/NotificationList';
import { getAllNotifications } from './services/notificationService';
import './App.css';

function App() {
  const [notifications, setNotifications] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch notifications on component mount
  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    setIsLoading(true);
    setError(null);
    
    try {
      const data = await getAllNotifications();
      // Sort by timestamp descending (newest first)
      const sortedData = data.sort((a, b) => 
        new Date(b.timestamp) - new Date(a.timestamp)
      );
      setNotifications(sortedData);
    } catch (err) {
      setError(err.message || 'Failed to load notifications');
      console.error('Error fetching notifications:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNotificationCreated = (newNotification) => {
    // Add new notification to the top of the list
    setNotifications(prev => [newNotification, ...prev]);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Notification System</h1>
        <p className="app-subtitle">Create and manage your notifications</p>
      </header>

      <main className="app-main">
        {error && (
          <div className="app-error">
            <strong>Error:</strong> {error}
            <button onClick={fetchNotifications} className="retry-button">
              Retry
            </button>
          </div>
        )}

        <NotificationForm onNotificationCreated={handleNotificationCreated} />
        
        <NotificationList 
          notifications={notifications} 
          isLoading={isLoading} 
        />
      </main>

      <footer className="app-footer">
        <p>Notification POC Website © 2025</p>
      </footer>
    </div>
  );
}

export default App;
