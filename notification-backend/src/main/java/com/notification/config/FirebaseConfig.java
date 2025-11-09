package com.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
    
    @Value("${firebase.service-account-key-path:}")
    private String serviceAccountKeyPath;
    
    @Value("${firebase.service-account-key-base64:}")
    private String serviceAccountKeyBase64;
    
    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccountStream = getServiceAccountStream();
            
            if (serviceAccountStream == null) {
                logger.warn("Firebase service account key not configured. Push notifications will not work.");
                logger.warn("Set either 'firebase.service-account-key-path' or 'firebase.service-account-key-base64'");
                return;
            }
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();
            
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase initialized successfully");
            }
        } catch (IOException e) {
            logger.error("Failed to initialize Firebase: {}", e.getMessage());
            logger.warn("Push notifications will not work without proper Firebase configuration");
        }
    }
    
    private InputStream getServiceAccountStream() throws IOException {
        // Try base64 encoded credentials first (for cloud deployment)
        if (serviceAccountKeyBase64 != null && !serviceAccountKeyBase64.isEmpty()) {
            logger.info("Loading Firebase credentials from base64 environment variable");
            byte[] decodedKey = Base64.getDecoder().decode(serviceAccountKeyBase64);
            return new ByteArrayInputStream(decodedKey);
        }
        
        // Fall back to file path (for local development)
        if (serviceAccountKeyPath != null && !serviceAccountKeyPath.isEmpty()) {
            logger.info("Loading Firebase credentials from file: {}", serviceAccountKeyPath);
            return new FileInputStream(serviceAccountKeyPath);
        }
        
        return null;
    }
}
