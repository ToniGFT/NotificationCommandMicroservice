package com.workshop.notification.application.service;

import com.workshop.notification.application.services.NotificationCommandServiceImpl;
import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.entities.Action;
import com.workshop.notification.domain.model.entities.Recipient;
import com.workshop.notification.domain.model.valueobjects.Contact;
import com.workshop.notification.domain.model.valueobjects.enums.NotificationType;
import com.workshop.notification.domain.model.valueobjects.enums.Severity;
import com.workshop.notification.domain.repository.NotificationCommandRepository;
import com.workshop.notification.infraestructure.Route.model.aggregates.Route;
import com.workshop.notification.infraestructure.Route.service.RouteService;
import com.workshop.notification.infraestructure.Vehicle.model.aggregates.Vehicle;
import com.workshop.notification.infraestructure.Vehicle.service.VehicleService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.workshop.notification.domain.model.valueobjects.enums.ActionStatus.COMPLETED;
import static com.workshop.notification.domain.model.valueobjects.enums.ActionType.SEND_EMAIL;
import static com.workshop.notification.domain.model.valueobjects.enums.RecipientType.PASSENGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationCommandServiceImplTest {

    @InjectMocks
    private NotificationCommandServiceImpl notificationCommandService;

    @Mock
    private NotificationCommandRepository notificationCommandRepository;
    @Mock
    private RouteService routeService;
    @Mock
    private VehicleService vehicleService;

    private Notification notification;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Contact contact = new Contact("johndoe@example.com", "603378977");

        List<Recipient> recipients = Arrays.asList(
                new Recipient(ObjectId.get(), PASSENGER, contact),
                new Recipient(ObjectId.get(), PASSENGER, contact)
        );

        List<Action> actions = Arrays.asList(
                Action.builder()
                        .actionId(new ObjectId())
                        .type(SEND_EMAIL)
                        .status(COMPLETED)
                        .timestamp(LocalDateTime.now())
                        .errormessage("")
                        .build(),
                Action.builder()
                        .actionId(new ObjectId())
                        .type(SEND_EMAIL)
                        .status(COMPLETED)
                        .timestamp(LocalDateTime.now().plusMinutes(1))
                        .errormessage("Action still in progress")
                        .build()
        );

        notification = Notification.builder()
                .notificationId(ObjectId.get())
                .timestamp(LocalDateTime.now())
                .type(NotificationType.EMERGENCY)  // Asigna el tipo de notificación
                .title("New Vehicle Alert")
                .message("This is an important notification regarding vehicle status.")
                .severity(Severity.HIGH)  // Asigna la severidad
                .routeId(new ObjectId())
                .vehicleId(new ObjectId())
                .recipients(recipients)
                .actions(actions)
                .build();
    }

    @Test
    public void testCreateNotificationExists() {
        when(routeService.getRouteById(any(String.class))).thenReturn(Mono.just(new Route()));
        when(vehicleService.getVehicleById(any(String.class))).thenReturn(Mono.just(new Vehicle()));
        when(notificationCommandRepository.save(any(Notification.class))).thenReturn(Mono.just(notification));

        Mono<Notification> response = notificationCommandService.createNotification(notification);

        assertEquals(notification, response.block());
        verify(notificationCommandRepository, times(1)).save(any(Notification.class));
        verify(routeService, times(1)).getRouteById(any(String.class));
    }

    @Test
    public void testCreateNotificationNotificationNotFound() {

        when(routeService.getRouteById(any(String.class))).thenReturn(Mono.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationCommandService.createNotification(notification).block();
        });

        assertEquals("Route not found with id: " + notification.getRouteId().toString(), exception.getMessage());
        verify(notificationCommandRepository, times(0)).save(any(Notification.class));
        verify(routeService, times(1)).getRouteById(any(String.class));
    }
}