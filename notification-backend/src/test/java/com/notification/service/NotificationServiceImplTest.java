package com.notification.service;

import com.notification.dto.CreateNotificationRequest;
import com.notification.dto.NotificationDTO;
import com.notification.model.Notification;
import com.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @BeforeEach
    void setUp() {
        // Setup is handled by @ExtendWith(MockitoExtension.class)
    }

    @Test
    void createNotification_shouldGenerateUUIDAndTimestamp() {
        // Arrange
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setTitle("Test Title");
        request.setMessage("Test Message");

        // Mock repository to return the saved notification
        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        NotificationDTO result = notificationService.createNotification(request);

        // Assert
        assertNotNull(result.getId(), "ID should be generated");
        assertFalse(result.getId().isEmpty(), "ID should not be empty");
        assertNotNull(result.getTimestamp(), "Timestamp should be generated");
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Message", result.getMessage());

        // Verify UUID format (basic check)
        assertTrue(result.getId().matches(
            "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
            "ID should be a valid UUID format");

        // Verify timestamp is in ISO format and recent
        assertDoesNotThrow(() -> LocalDateTime.parse(result.getTimestamp(), ISO_FORMATTER),
            "Timestamp should be in ISO format");

        // Verify repository save was called
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getAllNotifications_shouldTransformEntitiesToDTOs() {
        // Arrange
        LocalDateTime timestamp1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2024, 1, 2, 15, 30);

        Notification notification1 = new Notification(
            "id-1",
            "Title 1",
            "Message 1",
            timestamp1
        );

        Notification notification2 = new Notification(
            "id-2",
            "Title 2",
            "Message 2",
            timestamp2
        );

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findAll()).thenReturn(notifications);

        // Act
        List<NotificationDTO> result = notificationService.getAllNotifications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first notification transformation
        NotificationDTO dto1 = result.get(0);
        assertEquals("id-1", dto1.getId());
        assertEquals("Title 1", dto1.getTitle());
        assertEquals("Message 1", dto1.getMessage());
        assertEquals(timestamp1.format(ISO_FORMATTER), dto1.getTimestamp());

        // Verify second notification transformation
        NotificationDTO dto2 = result.get(1);
        assertEquals("id-2", dto2.getId());
        assertEquals("Title 2", dto2.getTitle());
        assertEquals("Message 2", dto2.getMessage());
        assertEquals(timestamp2.format(ISO_FORMATTER), dto2.getTimestamp());

        // Verify repository findAll was called
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void getAllNotifications_shouldReturnEmptyListWhenNoNotifications() {
        // Arrange
        when(notificationRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<NotificationDTO> result = notificationService.getAllNotifications();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepository, times(1)).findAll();
    }
}
