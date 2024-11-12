package com.workshop.notification.application.services;

import com.workshop.notification.domain.model.aggregates.Notification;
import reactor.core.publisher.Mono;

public interface NotificationCommandService {

    Mono<Notification> createNotification(Notification notification);

}

