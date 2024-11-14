package com.workshop.notification.application.response.service;

import com.workshop.notification.application.response.builder.NotificationResponseBuilder;
import com.workshop.notification.domain.model.aggregates.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class NotificationResponseServiceTest {

    private NotificationResponseService notificationResponseService;

    @BeforeEach
    void setUp() {
        notificationResponseService = new NotificationResponseService();
    }

    @Test
    void buildCreatedResponse_ShouldReturnCreatedResponse() {
        Notification notification = new Notification();
        URI location = URI.create("/notifications/" + notification.getNotificationId());  // Construye la URI del recurso creado
        ResponseEntity<Notification> expectedResponse = ResponseEntity.created(location).body(notification);  // Usa .body() para añadir el cuerpo


        try (MockedStatic<NotificationResponseBuilder> mockedStatic = mockStatic(NotificationResponseBuilder.class)) {
            mockedStatic.when(() -> NotificationResponseBuilder.generateCreatedResponse(any(Notification.class)))
                    .thenReturn(expectedResponse);

            Mono<ResponseEntity<Notification>> responseMono = notificationResponseService.buildCreatedResponse(notification);

            StepVerifier.create(responseMono)
                    .expectNext(expectedResponse)
                    .verifyComplete();
        }
    }
}