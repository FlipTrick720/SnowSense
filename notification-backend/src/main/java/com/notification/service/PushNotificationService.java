package com.notification.service;

import com.notification.model.Notification;

public interface PushNotificationService {
    void sendPushNotification(Notification notification);
    void subscribeToken(String token);
    void unsubscribeToken(String token);
}
