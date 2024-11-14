package com.workshop.notification.domain.validation;

import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.validation.NotificationValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificationValidatorTest {

    private Validator validator;
    private NotificationValidator notificationValidator;

    @BeforeEach
    void setUp() {
        validator = Mockito.mock(Validator.class);
        notificationValidator = new NotificationValidator(validator);
    }

    @Test
    void validate_ShouldThrowException_WhenValidationFails() {
        Notification notification = new Notification();
        Set<ConstraintViolation<Notification>> violations = new HashSet<>();

        ConstraintViolation<Notification> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid vehicle data");
        violations.add(violation);

        when(validator.validate(notification)).thenReturn(violations);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationValidator.validate(notification);
        });

        assertTrue(exception.getMessage().contains("Validation errors: Invalid vehicle data"));
    }

    @Test
    void validate_ShouldNotThrowException_WhenValidationPasses() {
        Notification notification = new Notification();
        Set<ConstraintViolation<Notification>> violations = new HashSet<>();

        when(validator.validate(notification)).thenReturn(violations);

        notificationValidator.validate(notification);
    }
}
