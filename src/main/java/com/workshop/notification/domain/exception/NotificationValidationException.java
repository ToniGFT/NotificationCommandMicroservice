package com.workshop.notification.domain.exception;

public class NotificationValidationException extends RuntimeException {
    public NotificationValidationException(String message) {
        super(message);
    }
}
