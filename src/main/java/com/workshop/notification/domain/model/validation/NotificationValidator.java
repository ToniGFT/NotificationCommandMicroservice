package com.workshop.notification.domain.model.validation;

import com.workshop.notification.domain.model.aggregates.Notification;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationValidator {

    private final Validator validator;

    public NotificationValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(Notification notification) {
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        if (!violations.isEmpty()) {
            String errorMessages = formatValidationErrors(violations);
            throw new IllegalArgumentException("Validation errors: " + errorMessages);
        }
    }

    private String formatValidationErrors(Set<ConstraintViolation<Notification>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
