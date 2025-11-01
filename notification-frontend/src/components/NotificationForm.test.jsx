import { describe, it, expect, vi } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import NotificationForm from './NotificationForm';
import * as notificationService from '../services/notificationService';

// Mock the notification service
vi.mock('../services/notificationService');

describe('NotificationForm', () => {
  it('renders form with input fields', () => {
    render(<NotificationForm />);
    
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/message/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /create notification/i })).toBeInTheDocument();
  });

  it('displays validation errors for empty fields', async () => {
    const user = userEvent.setup();
    render(<NotificationForm />);
    
    const submitButton = screen.getByRole('button', { name: /create notification/i });
    await user.click(submitButton);
    
    expect(screen.getByText(/title is required/i)).toBeInTheDocument();
    expect(screen.getByText(/message is required/i)).toBeInTheDocument();
  });

  it('displays validation error when title exceeds 100 characters', async () => {
    const user = userEvent.setup();
    render(<NotificationForm />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const longTitle = 'a'.repeat(101);
    
    await user.type(titleInput, longTitle);
    await user.click(screen.getByRole('button', { name: /create notification/i }));
    
    expect(screen.getByText(/title must not exceed 100 characters/i)).toBeInTheDocument();
  });

  it('displays validation error when message exceeds 500 characters', async () => {
    const user = userEvent.setup();
    render(<NotificationForm />);
    
    const messageInput = screen.getByLabelText(/message/i);
    const longMessage = 'a'.repeat(501);
    
    await user.type(messageInput, longMessage);
    await user.click(screen.getByRole('button', { name: /create notification/i }));
    
    expect(screen.getByText(/message must not exceed 500 characters/i)).toBeInTheDocument();
  });

  it('calls API service when form is submitted with valid data', async () => {
    const user = userEvent.setup();
    const mockNotification = {
      id: '123',
      title: 'Test Title',
      message: 'Test Message',
      timestamp: new Date().toISOString()
    };
    
    notificationService.createNotification.mockResolvedValue(mockNotification);
    
    render(<NotificationForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Title');
    await user.type(screen.getByLabelText(/message/i), 'Test Message');
    await user.click(screen.getByRole('button', { name: /create notification/i }));
    
    await waitFor(() => {
      expect(notificationService.createNotification).toHaveBeenCalledWith({
        title: 'Test Title',
        message: 'Test Message'
      });
    });
  });

  it('displays success message after successful submission', async () => {
    const user = userEvent.setup();
    const mockNotification = {
      id: '123',
      title: 'Test Title',
      message: 'Test Message',
      timestamp: new Date().toISOString()
    };
    
    notificationService.createNotification.mockResolvedValue(mockNotification);
    
    render(<NotificationForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Title');
    await user.type(screen.getByLabelText(/message/i), 'Test Message');
    await user.click(screen.getByRole('button', { name: /create notification/i }));
    
    await waitFor(() => {
      expect(screen.getByText(/notification created successfully/i)).toBeInTheDocument();
    });
  });

  it('clears form fields after successful submission', async () => {
    const user = userEvent.setup();
    const mockNotification = {
      id: '123',
      title: 'Test Title',
      message: 'Test Message',
      timestamp: new Date().toISOString()
    };
    
    notificationService.createNotification.mockResolvedValue(mockNotification);
    
    render(<NotificationForm />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const messageInput = screen.getByLabelText(/message/i);
    
    await user.type(titleInput, 'Test Title');
    await user.type(messageInput, 'Test Message');
    await user.click(screen.getByRole('button', { name: /create notification/i }));
    
    await waitFor(() => {
      expect(titleInput).toHaveValue('');
      expect(messageInput).toHaveValue('');
    });
  });

  it('displays error message when API call fails', async () => {
    const user = userEvent.setup();
    const errorMessage = 'Failed to create notification';
    
    notificationService.createNotification.mockRejectedValue(new Error(errorMessage));
    
    render(<NotificationForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Title');
    await user.type(screen.getByLabelText(/message/i), 'Test Message');
    await user.click(screen.getByRole('button', { name: /create notification/i }));
    
    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
  });
});
