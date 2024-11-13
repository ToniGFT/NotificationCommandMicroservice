package com.workshop.notification.application.controller;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.workshop.notification.application.response.service.NotificationResponseService;
import com.workshop.notification.application.services.NotificationCommandService;
import com.workshop.notification.domain.model.aggregates.Notification;
import com.workshop.notification.domain.model.entities.Recipient;
import com.workshop.notification.domain.model.valueobjects.Contact;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.workshop.notification.domain.model.valueobjects.enums.ActionStatus.COMPLETED;
import static com.workshop.notification.domain.model.valueobjects.enums.ActionType.SEND_EMAIL;
import static com.workshop.notification.domain.model.valueobjects.enums.RecipientType.PASSENGER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.workshop.notification.domain.model.entities.Action;
import com.workshop.notification.domain.model.valueobjects.enums.NotificationType;
import com.workshop.notification.domain.model.valueobjects.enums.Severity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class NotificationCommandControllerTest {

    @InjectMocks
    private NotificationCommandController notificationCommandController;

    @Mock
    private NotificationCommandService notificationCommandService;

    @Mock
    private NotificationResponseService notificationResponseService;

    private Notification notification;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

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

        Notification.builder()
                .notificationId(ObjectId.get())
                .timestamp(LocalDateTime.now())
                .type(NotificationType.EMERGENCY)  // Asigna el tipo de notificación
                .title("New Vehicle Alert")
                .message("This is an important notification regarding vehicle status.")
                .severity(Severity.HIGH)  // Asigna la severidad
                .routeId("R1234")
                .vehicleId("V5678")
                .recipients(recipients)
                .actions(actions)
                .build();

        Logger logger = (Logger) LoggerFactory.getLogger(NotificationCommandController.class);
        logger.addAppender(mockAppender);
    }

    @Test
    public void testCreateNotificationLogsOnSuccess() {
        when(notificationCommandService.createNotification(any(Notification.class))).thenReturn(Mono.just(notification));
        when(notificationResponseService.buildCreatedResponse(any(Notification.class))).thenReturn(Mono.just(ResponseEntity.ok(notification)));

        Mono<ResponseEntity<Notification>> response = notificationCommandController.createNotification(notification);
        response.block();

        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());

        boolean logEncontrado = captorLoggingEvent.getAllValues().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Successfully created notification with ID: " + notification.getNotificationId()));

        assertTrue(logEncontrado, "El log esperado no fue encontrado.");
    }
}