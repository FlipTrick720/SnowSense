package com.notification.service;

import com.google.firebase.FirebaseApp;
import com.notification.model.FcmToken;
import com.notification.model.Notification;
import com.notification.repository.FcmTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PushNotificationServiceImplTest {

    @Mock
    private FcmTokenRepository fcmTokenRepository;

    @InjectMocks
    private PushNotificationServiceImpl service;

    @Test
    void sendPushNotification_shouldSkipWhenFirebaseNotInitialized() {
        Notification notification = new Notification("id", "T", "M", LocalDateTime.now());

        try (MockedStatic<FirebaseApp> firebaseApp = mockStatic(FirebaseApp.class)) {
            firebaseApp.when(FirebaseApp::getApps).thenReturn(Collections.emptyList());

            service.sendPushNotification(notification);

            verifyNoInteractions(fcmTokenRepository);
        }
    }

    @Test
    void sendPushNotification_shouldSkipWhenNoTokens() {
        Notification notification = new Notification("id", "T", "M", LocalDateTime.now());

        try (MockedStatic<FirebaseApp> firebaseApp = mockStatic(FirebaseApp.class)) {
            firebaseApp.when(FirebaseApp::getApps).thenReturn(Collections.singletonList(mock(FirebaseApp.class)));
            when(fcmTokenRepository.findAll()).thenReturn(Collections.emptyList());

            service.sendPushNotification(notification);

            verify(fcmTokenRepository).findAll();
            verify(fcmTokenRepository, never()).delete(anyString());
        }
    }

    @Test
    void subscribeToken_shouldSaveWhenNotExists() {
        when(fcmTokenRepository.exists("token-1234567890")).thenReturn(false);

        service.subscribeToken("token-1234567890");

        verify(fcmTokenRepository).save(any(FcmToken.class));
    }

    @Test
    void subscribeToken_shouldNotSaveWhenAlreadyExists() {
        when(fcmTokenRepository.exists("token-1234567890")).thenReturn(true);

        service.subscribeToken("token-1234567890");

        verify(fcmTokenRepository, never()).save(any());
    }

    @Test
    void unsubscribeToken_shouldDelete() {
        service.unsubscribeToken("token-1234567890");

        verify(fcmTokenRepository).delete("token-1234567890");
    }
}
