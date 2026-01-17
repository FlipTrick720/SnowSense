package com.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.dto.CreateNotificationRequest;
import com.notification.dto.NotificationDTO;
import com.notification.dto.SubscriptionRequest;
import com.notification.service.NotificationService;
import com.notification.service.PushNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private PushNotificationService pushNotificationService;

    @Test
    void createNotification_shouldReturnCreatedNotification() throws Exception {
        NotificationDTO dto = new NotificationDTO("id-1", "T", "M", "2024-01-01T10:00:00");
        when(notificationService.createNotification(any(CreateNotificationRequest.class))).thenReturn(dto);

        CreateNotificationRequest request = new CreateNotificationRequest("T", "M");

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("id-1"))
                .andExpect(jsonPath("$.title").value("T"))
                .andExpect(jsonPath("$.message").value("M"))
                .andExpect(jsonPath("$.timestamp").value("2024-01-01T10:00:00"));

        verify(notificationService).createNotification(any(CreateNotificationRequest.class));
    }

    @Test
    void createNotification_shouldReturnBadRequestWhenValidationFails() throws Exception {
        // empty title triggers @NotBlank
        CreateNotificationRequest request = new CreateNotificationRequest("", "M");

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void createNotification_shouldReturn500WhenServiceThrows() throws Exception {
        doThrow(new RuntimeException("boom"))
                .when(notificationService)
                .createNotification(any(CreateNotificationRequest.class));

        CreateNotificationRequest request = new CreateNotificationRequest("T", "M");

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.details[0]").value("boom"));
    }

    @Test
    void getAllNotifications_shouldReturnList() throws Exception {
        when(notificationService.getAllNotifications()).thenReturn(List.of(
                new NotificationDTO("id-1", "T1", "M1", "2024-01-01T10:00:00"),
                new NotificationDTO("id-2", "T2", "M2", "2024-01-02T10:00:00")
        ));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("id-1"))
                .andExpect(jsonPath("$[1].id").value("id-2"));

        verify(notificationService).getAllNotifications();
    }

    @Test
    void subscribe_shouldCallServiceAndReturnSuccess() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest("token-1234567890");

        mockMvc.perform(post("/api/notifications/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Successfully subscribed to push notifications"));

        verify(pushNotificationService).subscribeToken(eq("token-1234567890"));
    }

    @Test
    void unsubscribe_shouldCallServiceAndReturnSuccess() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest("token-1234567890");

        mockMvc.perform(post("/api/notifications/unsubscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Successfully unsubscribed from push notifications"));

        verify(pushNotificationService).unsubscribeToken(eq("token-1234567890"));
    }

    @Test
    void subscribe_shouldReturnBadRequestWhenTokenMissing() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest("");

        mockMvc.perform(post("/api/notifications/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }
}
