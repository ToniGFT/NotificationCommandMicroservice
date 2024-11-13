package com.workshop.notification.application.services;

import com.workshop.notification.domain.exception.NotificationValidationException;
import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.validation.NotificationValidator;
import com.workshop.notification.domain.repository.NotificationCommandRepository;
import com.workshop.notification.infraestructure.service.RouteService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationCommandRepository notificationRepository;
    private final NotificationValidator notificationValidator;
    private final RouteService routeService;

    public NotificationCommandServiceImpl(NotificationCommandRepository notificationRepository, NotificationValidator notificationValidator, RouteService routeService) {
        this.notificationRepository = notificationRepository;
        this.notificationValidator = notificationValidator;
        this.routeService = routeService;
    }

    @Override
    public Mono<Notification> createNotification(Notification notification) {
        return routeService.getRouteById(notification.getRouteId())
                .switchIfEmpty(Mono.error(new RuntimeException("Route not found with id: " + notification.getRouteId())))
                .then(Mono.just(notification))
                .doOnNext(notificationValidator::validate)
                .onErrorMap(e -> new NotificationValidationException("Invalid notification data: " + e.getMessage()))
                .flatMap(notificationRepository::save);

    }
}
