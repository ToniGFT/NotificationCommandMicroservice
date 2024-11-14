package com.workshop.notification.application.services;

import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.validation.NotificationValidator;
import com.workshop.notification.domain.repository.NotificationCommandRepository;

import com.workshop.notification.infraestructure.Route.service.RouteService;
import com.workshop.notification.infraestructure.Vehicle.service.VehicleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationCommandRepository notificationRepository;
    private final NotificationValidator notificationValidator;
    private final RouteService routeService;
    private final VehicleService vehicleService;

    public NotificationCommandServiceImpl(NotificationCommandRepository notificationRepository, NotificationValidator notificationValidator, RouteService routeService, VehicleService vehicleService) {
        this.notificationRepository = notificationRepository;
        this.notificationValidator = notificationValidator;
        this.routeService = routeService;
        this.vehicleService = vehicleService;
    }

    @Override
    public Mono<Notification> createNotification(Notification notification) {
        return routeService.getRouteById(notification.getRouteId().toHexString())
                .switchIfEmpty(Mono.error(new RuntimeException("Route not found with id: " + notification.getRouteId().toString())))
                .flatMap(route -> {
                    return vehicleService.getVehicleById(notification.getVehicleId().toHexString())
                            .switchIfEmpty(Mono.error(new RuntimeException("Vehicle not found with id: " + notification.getVehicleId().toString())))
                            .flatMap(vehicle -> {
                                return notificationRepository.save(notification);
                            });
                });
    }
}
