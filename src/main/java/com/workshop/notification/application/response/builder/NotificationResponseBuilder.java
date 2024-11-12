package com.workshop.notification.application.response.builder;

import com.workshop.notification.domain.model.aggregates.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NotificationResponseBuilder {

    public static ResponseEntity<Notification> generateCreatedResponse(Notification notification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }
}
