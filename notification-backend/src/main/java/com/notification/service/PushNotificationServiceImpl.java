package com.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import com.notification.model.FcmToken;
import com.notification.model.Notification;
import com.notification.repository.FcmTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);
    
    private final FcmTokenRepository fcmTokenRepository;
    
    public PushNotificationServiceImpl(FcmTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }
    
    @Override
    public void sendPushNotification(Notification notification) {
        if (FirebaseApp.getApps().isEmpty()) {
            logger.warn("Firebase not initialized. Skipping push notification.");
            return;
        }
        
        List<FcmToken> tokens = fcmTokenRepository.findAll();
        
        if (tokens.isEmpty()) {
            logger.info("No FCM tokens registered. Skipping push notification.");
            return;
        }
        
        logger.info("Sending push notification to {} subscribers", tokens.size());
        
        // Build notification payload
        com.google.firebase.messaging.Notification fcmNotification = 
            com.google.firebase.messaging.Notification.builder()
                .setTitle(notification.getTitle())
                .setBody(notification.getMessage())
                .build();
        
        // Add custom data
        Map<String, String> data = new HashMap<>();
        data.put("notificationId", notification.getId());
        data.put("timestamp", notification.getTimestamp().toString());
        
        // Send to each token
        for (FcmToken fcmToken : tokens) {
            try {
                Message message = Message.builder()
                        .setToken(fcmToken.getToken())
                        .setNotification(fcmNotification)
                        .putAllData(data)
                        .setWebpushConfig(WebpushConfig.builder()
                                .setNotification(WebpushNotification.builder()
                                        .setTitle(notification.getTitle())
                                        .setBody(notification.getMessage())
                                        .setIcon("/notification-icon.png")
                                        .build())
                                .build())
                        .build();
                
                String response = FirebaseMessaging.getInstance().send(message);
                logger.info("Successfully sent push notification: {}", response);
                
            } catch (FirebaseMessagingException e) {
                logger.error("Failed to send push notification to token {}: {}", 
                        fcmToken.getToken().substring(0, 10) + "...", e.getMessage());
                
                // Remove invalid tokens
                if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT ||
                    e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    logger.info("Removing invalid token");
                    fcmTokenRepository.delete(fcmToken.getToken());
                }
            }
        }
    }
    
    @Override
    public void subscribeToken(String token) {
        if (!fcmTokenRepository.exists(token)) {
            FcmToken fcmToken = new FcmToken(token);
            fcmTokenRepository.save(fcmToken);
            logger.info("Subscribed new FCM token: {}...", token.substring(0, 10));
        } else {
            logger.info("FCM token already subscribed");
        }
    }
    
    @Override
    public void unsubscribeToken(String token) {
        fcmTokenRepository.delete(token);
        logger.info("Unsubscribed FCM token: {}...", token.substring(0, 10));
    }
}
