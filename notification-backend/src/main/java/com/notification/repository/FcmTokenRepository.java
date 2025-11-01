package com.notification.repository;

import com.notification.model.FcmToken;

import java.util.List;

public interface FcmTokenRepository {
    void save(FcmToken token);
    void delete(String token);
    List<FcmToken> findAll();
    boolean exists(String token);
}
