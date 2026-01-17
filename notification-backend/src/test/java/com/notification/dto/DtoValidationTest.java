package com.notification.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void afterAll() {
        factory.close();
    }

    @Test
    void createNotificationRequest_shouldPassValidationForValidData() {
        CreateNotificationRequest req = new CreateNotificationRequest("Title", "Message");
        Set<ConstraintViolation<CreateNotificationRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createNotificationRequest_shouldFailWhenTitleBlank() {
        CreateNotificationRequest req = new CreateNotificationRequest("", "Message");
        Set<ConstraintViolation<CreateNotificationRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void createNotificationRequest_shouldFailWhenMessageTooLong() {
        String longMsg = "x".repeat(501);
        CreateNotificationRequest req = new CreateNotificationRequest("Title", longMsg);
        Set<ConstraintViolation<CreateNotificationRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("message")));
    }

    @Test
    void subscriptionRequest_shouldFailWhenTokenBlank() {
        SubscriptionRequest req = new SubscriptionRequest(" ");
        Set<ConstraintViolation<SubscriptionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("token")));
    }

    @Test
    void errorResponse_singleArgCtor_shouldSetEmptyDetails() {
        ErrorResponse res = new ErrorResponse("err");
        assertEquals("err", res.getError());
        assertNotNull(res.getDetails());
        assertTrue(res.getDetails().isEmpty());
    }
}
