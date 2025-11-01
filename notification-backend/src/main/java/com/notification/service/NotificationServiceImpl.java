package com.notification.service;

import com.notification.dto.CreateNotificationRequest;
import com.notification.dto.NotificationDTO;
import com.notification.model.Notification;
import com.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of NotificationService that handles notification business logic.
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final PushNotificationService pushNotificationService;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   PushNotificationService pushNotificationService) {
        this.notificationRepository = notificationRepository;
        this.pushNotificationService = pushNotificationService;
    }
    
    @Override
    public NotificationDTO createNotification(CreateNotificationRequest request) {
        // Generate UUID and timestamp
        String id = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Create entity
        Notification notification = new Notification(
            id,
            request.getTitle(),
            request.getMessage(),
            timestamp
        );
        
        // Save to repository
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send push notification to all subscribers
        pushNotificationService.sendPushNotification(savedNotification);
        
        // Transform to DTO
        return toDTO(savedNotification);
    }
    
    @Override
    public List<NotificationDTO> getAllNotifications() {
        // Retrieve all notifications and transform to DTOs
        return notificationRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Transforms a Notification entity to a NotificationDTO.
     *
     * @param notification the entity to transform
     * @return the DTO representation
     */
    private NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
            notification.getId(),
            notification.getTitle(),
            notification.getMessage(),
            notification.getTimestamp().format(ISO_FORMATTER)
        );
    }
}
