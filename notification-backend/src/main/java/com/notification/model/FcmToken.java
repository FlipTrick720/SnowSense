package com.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FcmToken {
    private String token;
    private LocalDateTime subscribedAt;
    
    public FcmToken(String token) {
        this.token = token;
        this.subscribedAt = LocalDateTime.now();
    }
}
