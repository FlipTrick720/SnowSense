package com.notification.repository;

import com.notification.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryNotificationRepository implements NotificationRepository {
    
    private final ConcurrentHashMap<String, Notification> storage = new ConcurrentHashMap<>();
    
    @Override
    public Notification save(Notification notification) {
        storage.put(notification.getId(), notification);
        return notification;
    }
    
    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public void clear() {
        storage.clear();
    }
}
