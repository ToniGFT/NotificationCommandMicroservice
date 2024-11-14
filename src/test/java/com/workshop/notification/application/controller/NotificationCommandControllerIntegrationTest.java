package com.workshop.notification.application.controller;

import com.workshop.notification.application.response.service.NotificationResponseService;
import com.workshop.notification.application.services.NotificationCommandService;
import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.entities.Action;
import com.workshop.notification.domain.model.entities.Recipient;
import com.workshop.notification.domain.model.valueobjects.Contact;
import com.workshop.notification.domain.model.valueobjects.enums.NotificationType;
import com.workshop.notification.domain.model.valueobjects.enums.Severity;
import com.workshop.notification.infraestructure.models.aggregates.Route;
import com.workshop.notification.infraestructure.service.RouteService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.workshop.notification.domain.model.valueobjects.enums.ActionStatus.COMPLETED;
import static com.workshop.notification.domain.model.valueobjects.enums.ActionType.SEND_EMAIL;
import static com.workshop.notification.domain.model.valueobjects.enums.RecipientType.PASSENGER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = NotificationCommandController.class)
public class NotificationCommandControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NotificationCommandService notificationCommandService;

    @MockBean
    private NotificationResponseService notificationResponseService;

    @MockBean
    private RouteService routeService;

    private Notification testNotification;

    @BeforeEach
    public void setUp() {

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

        testNotification = Notification.builder()
                .notificationId(ObjectId.get())
                .timestamp(LocalDateTime.now())
                .type(NotificationType.EMERGENCY)  // Asigna el tipo de notificación
                .title("New Vehicle Alert")
                .message("This is an important notification regarding vehicle status.")
                .severity(Severity.HIGH)  // Asigna la severidad
                .routeId(new ObjectId("67346708d9edc8617fbf53d2"))
                .vehicleId(new ObjectId("67346708d9edc8617fbf53d2"))
                .recipients(recipients)
                .actions(actions)
                .build();
    }

    @Test
    public void testCreateVehicle() {
        Notification newNotification = testNotification;

        when(notificationCommandService.createNotification(any(Notification.class)))
                .thenReturn(Mono.just(newNotification));

        when(notificationResponseService.buildCreatedResponse(any(Notification.class)))
                .thenReturn(Mono.just(ResponseEntity.ok(newNotification)));

        when(routeService.getRouteById(any(String.class))).thenReturn(Mono.just(new Route()));

        webTestClient.post()
                .uri("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newNotification)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("New Vehicle Alert")
                .jsonPath("$.message").isEqualTo("This is an important notification regarding vehicle status.");
    }
}
