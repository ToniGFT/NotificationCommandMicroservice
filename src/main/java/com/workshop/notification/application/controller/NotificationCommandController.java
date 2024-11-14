package com.workshop.notification.application.controller;

import com.workshop.notification.application.response.service.NotificationResponseService;
import com.workshop.notification.application.services.NotificationCommandService;
import com.workshop.notification.domain.model.aggregates.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/notifications")
public class NotificationCommandController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationCommandController.class);
    private final NotificationCommandService notificationCommandService;
    private final NotificationResponseService notificationResponseService;

    public NotificationCommandController(NotificationCommandService notificationCommandService, NotificationResponseService notificationResponseService) {
        this.notificationCommandService = notificationCommandService;
        this.notificationResponseService = notificationResponseService;
    }

    @PostMapping
    public Mono<ResponseEntity<Notification>> createNotification(@RequestBody Notification notification) {
        logger.info("Attempting to create a new route with ID: {}", notification.getRouteId());
        return notificationCommandService.createNotification(notification)
                .flatMap(notificationResponseService::buildCreatedResponse)
                .doOnSuccess(response -> logger.info("Successfully created notification with ID: {}", notification.getNotificationId()));
    }

}
