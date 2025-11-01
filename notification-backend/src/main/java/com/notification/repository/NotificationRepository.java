package com.notification.repository;

import com.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findAll();
    Optional<Notification> findById(String id);
    void clear();
}
