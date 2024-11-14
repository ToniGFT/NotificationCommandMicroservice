package com.workshop.notification.application.response.builder;

import com.workshop.notification.domain.model.aggregates.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationResponseBuilderTest {

    @Test
    void generateCreatedResponse_ShouldReturnCreatedResponseWithNotification() {
        Notification notification = new Notification();

        ResponseEntity<Notification> responseEntity = NotificationResponseBuilder.generateCreatedResponse(notification);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(notification);
    }
}
