package com.workshop.notification.application.response.service;

import com.workshop.notification.application.response.builder.NotificationResponseBuilder;
import com.workshop.notification.domain.model.aggregates.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificationResponseService {

    public Mono<ResponseEntity<Notification>> buildCreatedResponse(Notification notification) {
        return Mono.fromCallable(() -> NotificationResponseBuilder.generateCreatedResponse(notification));
    }
}
