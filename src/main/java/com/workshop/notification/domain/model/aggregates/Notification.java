package com.workshop.notification.domain.model.aggregates;

import com.workshop.notification.domain.model.entities.Action;
import com.workshop.notification.domain.model.entities.Recipient;
import com.workshop.notification.domain.model.valueobjects.enums.NotificationType;
import com.workshop.notification.domain.model.valueobjects.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Notification")
public class Notification {

    @Id
    private ObjectId notificationId;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message must not exceed 500 characters")
    private String message;

    @NotNull(message = "Severity is required")
    private Severity severity;

    @NotBlank(message = "Route ID cannot be empty")
    private String routeId;

    @NotBlank(message = "Vehicle ID cannot be empty")
    private String vehicleId;

    @NotEmpty(message = "Recipients list cannot be empty")
    private List<Recipient> recipients;

    @NotEmpty(message = "Actions list cannot be empty")
    private List<Action> actions;
}
