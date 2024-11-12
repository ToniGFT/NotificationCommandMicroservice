package com.workshop.notification.application.services;

import com.workshop.notification.domain.exception.NotificationValidationException;
import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.validation.NotificationValidator;
import com.workshop.notification.domain.repository.NotificationCommandRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationCommandRepository notificationRepository;
    private final NotificationValidator notificationValidator;

    public NotificationCommandServiceImpl(NotificationCommandRepository notificationRepository, NotificationValidator notificationValidator) {
        this.notificationRepository = notificationRepository;
        this.notificationValidator = notificationValidator;
    }

    @Override
    public Mono<Notification> createNotification(Notification notification) {
        return Mono.just(notification)
                .doOnNext(notificationValidator::validate)
                .onErrorMap(e -> new NotificationValidationException("Invalid route data: " + e.getMessage()))
                .flatMap(notificationRepository::save);

    }
}
