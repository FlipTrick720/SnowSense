package com.notification.controller;

import com.notification.dto.CreateNotificationRequest;
import com.notification.dto.NotificationDTO;
import com.notification.dto.SubscriptionRequest;
import com.notification.model.Notification;
import com.notification.service.NotificationService;
import com.notification.service.PushNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final PushNotificationService pushNotificationService;

    public NotificationController(NotificationService notificationService,
                                   PushNotificationService pushNotificationService) {
        this.notificationService = notificationService;
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        NotificationDTO notification = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToPushNotifications(
            @Valid @RequestBody SubscriptionRequest request) {
        pushNotificationService.subscribeToken(request.getToken());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully subscribed to push notifications");
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribeFromPushNotifications(
            @Valid @RequestBody SubscriptionRequest request) {
        pushNotificationService.unsubscribeToken(request.getToken());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully unsubscribed from push notifications");
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test-broadcast")
    public ResponseEntity<Map<String, String>> testBroadcast(@RequestBody Map<String, String> payload) {
        Notification testNotification = new Notification();
        testNotification.setId(UUID.randomUUID().toString());
        testNotification.setTitle(payload.getOrDefault("title", "Test Notification"));
        testNotification.setMessage(payload.getOrDefault("message", "This is a test message from SnowSense"));
        testNotification.setTimestamp(LocalDateTime.now());

        pushNotificationService.sendPushNotification(testNotification);

        Map<String, String> response = new HashMap<>();
        response.put("status", "Sent to all active subscribers");
        return ResponseEntity.ok(response);
    }
}
