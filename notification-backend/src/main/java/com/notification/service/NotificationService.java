package com.notification.service;

import com.notification.dto.CreateNotificationRequest;
import com.notification.dto.NotificationDTO;
import java.util.List;

/**
 * Service interface for notification operations.
 * Defines business logic methods for creating and retrieving notifications.
 */
public interface NotificationService {
    
    /**
     * Creates a new notification from the provided request.
     *
     * @param request the notification creation request containing title and message
     * @return the created notification as a DTO
     */
    NotificationDTO createNotification(CreateNotificationRequest request);
    
    /**
     * Retrieves all notifications in the system.
     *
     * @return a list of all notifications as DTOs
     */
    List<NotificationDTO> getAllNotifications();
}
