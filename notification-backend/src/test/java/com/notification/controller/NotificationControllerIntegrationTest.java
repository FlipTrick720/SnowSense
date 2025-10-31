package com.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.dto.CreateNotificationRequest;
import com.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        // Clear repository before each test to ensure test isolation
        notificationRepository.clear();
    }

    @Test
    void testCreateNotification_WithValidData_Returns201() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest(
                "Test Notification",
                "This is a test message"
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Notification"))
                .andExpect(jsonPath("$.message").value("This is a test message"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testCreateNotification_WithBlankTitle_Returns400() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest(
                "",
                "This is a test message"
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(greaterThan(0))));
    }

    @Test
    void testCreateNotification_WithBlankMessage_Returns400() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest(
                "Test Notification",
                ""
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void testCreateNotification_WithTitleTooLong_Returns400() throws Exception {
        String longTitle = "a".repeat(101);
        CreateNotificationRequest request = new CreateNotificationRequest(
                longTitle,
                "This is a test message"
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void testCreateNotification_WithMessageTooLong_Returns400() throws Exception {
        String longMessage = "a".repeat(501);
        CreateNotificationRequest request = new CreateNotificationRequest(
                "Test Notification",
                longMessage
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void testGetAllNotifications_ReturnsEmptyList_Initially() throws Exception {
        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAllNotifications_ReturnsAllNotifications() throws Exception {
        // Create first notification
        CreateNotificationRequest request1 = new CreateNotificationRequest(
                "First Notification",
                "First message"
        );
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        // Create second notification
        CreateNotificationRequest request2 = new CreateNotificationRequest(
                "Second Notification",
                "Second message"
        );
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        // Retrieve all notifications
        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("First Notification", "Second Notification")))
                .andExpect(jsonPath("$[*].message", containsInAnyOrder("First message", "Second message")))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].timestamp").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].timestamp").exists());
    }

    @Test
    void testCreateNotification_WithNullTitle_Returns400() throws Exception {
        String requestJson = "{\"message\": \"Test message\"}";

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void testCreateNotification_WithNullMessage_Returns400() throws Exception {
        String requestJson = "{\"title\": \"Test title\"}";

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void testGetAllNotifications_VerifyJsonStructure() throws Exception {
        // Create a notification
        CreateNotificationRequest request = new CreateNotificationRequest(
                "Structure Test",
                "Testing JSON structure"
        );
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Verify JSON structure
        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].title").isString())
                .andExpect(jsonPath("$[0].message").isString())
                .andExpect(jsonPath("$[0].timestamp").isString());
    }
}
