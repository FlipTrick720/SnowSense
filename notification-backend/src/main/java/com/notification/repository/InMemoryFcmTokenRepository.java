package com.notification.repository;

import com.notification.model.FcmToken;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFcmTokenRepository implements FcmTokenRepository {
    
    private final ConcurrentHashMap<String, FcmToken> tokens = new ConcurrentHashMap<>();
    
    @Override
    public void save(FcmToken token) {
        tokens.put(token.getToken(), token);
    }
    
    @Override
    public void delete(String token) {
        tokens.remove(token);
    }
    
    @Override
    public List<FcmToken> findAll() {
        return new ArrayList<>(tokens.values());
    }
    
    @Override
    public boolean exists(String token) {
        return tokens.containsKey(token);
    }
}
