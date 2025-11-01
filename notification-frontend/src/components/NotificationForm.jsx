import { useState } from 'react';
import { createNotification } from '../services/notificationService';
import './NotificationForm.css';

function NotificationForm({ onNotificationCreated }) {
  const [formData, setFormData] = useState({
    title: '',
    message: ''
  });
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.title.trim()) {
      newErrors.title = 'Title is required';
    } else if (formData.title.length > 100) {
      newErrors.title = 'Title must not exceed 100 characters';
    }
    
    if (!formData.message.trim()) {
      newErrors.message = 'Message is required';
    } else if (formData.message.length > 500) {
      newErrors.message = 'Message must not exceed 500 characters';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear error for this field when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
    
    // Clear messages when user starts typing
    if (successMessage) setSuccessMessage('');
    if (errorMessage) setErrorMessage('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Clear previous messages
    setSuccessMessage('');
    setErrorMessage('');
    
    // Validate form
    if (!validateForm()) {
      return;
    }
    
    setIsSubmitting(true);
    
    try {
      const notification = await createNotification(formData);
      setSuccessMessage('Notification created successfully!');
      
      // Reset form
      setFormData({
        title: '',
        message: ''
      });
      setErrors({});
      
      // Notify parent component
      if (onNotificationCreated) {
        onNotificationCreated(notification);
      }
      
      // Clear success message after 3 seconds
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (error) {
      // Handle validation errors from backend
      if (error.details && error.details.length > 0) {
        setErrorMessage(error.details.join(', '));
      } else {
        setErrorMessage(error.message || 'Failed to create notification');
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="notification-form-container">
      <h2>Create Notification</h2>
      
      {successMessage && (
        <div className="alert alert-success">{successMessage}</div>
      )}
      
      {errorMessage && (
        <div className="alert alert-error">{errorMessage}</div>
      )}
      
      <form onSubmit={handleSubmit} className="notification-form">
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className={errors.title ? 'error' : ''}
            placeholder="Enter notification title"
            disabled={isSubmitting}
          />
          {errors.title && (
            <span className="error-message">{errors.title}</span>
          )}
          <span className="char-count">
            {formData.title.length}/100
          </span>
        </div>
        
        <div className="form-group">
          <label htmlFor="message">Message</label>
          <textarea
            id="message"
            name="message"
            value={formData.message}
            onChange={handleChange}
            className={errors.message ? 'error' : ''}
            placeholder="Enter notification message"
            rows="4"
            disabled={isSubmitting}
          />
          {errors.message && (
            <span className="error-message">{errors.message}</span>
          )}
          <span className="char-count">
            {formData.message.length}/500
          </span>
        </div>
        
        <button 
          type="submit" 
          className={`submit-button ${isSubmitting ? 'loading' : ''}`}
          disabled={isSubmitting}
        >
          {isSubmitting ? 'Creating...' : 'Create Notification'}
        </button>
      </form>
    </div>
  );
}

export default NotificationForm;
