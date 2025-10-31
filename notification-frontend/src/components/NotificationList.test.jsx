import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import NotificationList from './NotificationList';

describe('NotificationList', () => {
  const mockNotifications = [
    {
      id: '1',
      title: 'First Notification',
      message: 'This is the first notification',
      timestamp: '2025-01-15T10:30:00Z'
    },
    {
      id: '2',
      title: 'Second Notification',
      message: 'This is the second notification',
      timestamp: '2025-01-15T11:45:00Z'
    }
  ];

  it('renders notifications correctly', () => {
    render(<NotificationList notifications={mockNotifications} isLoading={false} />);
    
    expect(screen.getByText('First Notification')).toBeInTheDocument();
    expect(screen.getByText('This is the first notification')).toBeInTheDocument();
    expect(screen.getByText('Second Notification')).toBeInTheDocument();
    expect(screen.getByText('This is the second notification')).toBeInTheDocument();
  });

  it('displays empty state when no notifications exist', () => {
    render(<NotificationList notifications={[]} isLoading={false} />);
    
    expect(screen.getByText(/no notifications yet/i)).toBeInTheDocument();
  });

  it('displays loading state when isLoading is true', () => {
    render(<NotificationList notifications={[]} isLoading={true} />);
    
    expect(screen.getByText(/loading notifications/i)).toBeInTheDocument();
  });

  it('displays notification count correctly', () => {
    render(<NotificationList notifications={mockNotifications} isLoading={false} />);
    
    expect(screen.getByText('2 notifications')).toBeInTheDocument();
  });

  it('displays singular notification text when count is 1', () => {
    const singleNotification = [mockNotifications[0]];
    render(<NotificationList notifications={singleNotification} isLoading={false} />);
    
    expect(screen.getByText('1 notification')).toBeInTheDocument();
  });

  it('formats timestamp correctly', () => {
    render(<NotificationList notifications={mockNotifications} isLoading={false} />);
    
    // Check that timestamps are rendered (format may vary by locale)
    const timestamps = screen.getAllByText(/Jan|January/i);
    expect(timestamps.length).toBeGreaterThan(0);
  });
});
